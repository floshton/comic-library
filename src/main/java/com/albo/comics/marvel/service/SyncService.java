package com.albo.comics.marvel.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.albo.comics.marvel.vo.local.CharacterCreator;
import com.albo.comics.marvel.vo.local.CharactersInComicsReponse;
import com.albo.comics.marvel.vo.remote.character.Character;
import com.albo.comics.marvel.vo.remote.comicsByCharacter.Comic;
import com.albo.comics.marvel.vo.remote.comicsByCharacter.MarvelComicResponse;
import com.albo.comics.marvel.domain.CharacterDO;
import com.albo.comics.marvel.domain.ComicDO;
import com.albo.comics.marvel.repository.CharacterRepository;
import com.albo.comics.marvel.repository.ComicRepository;
import com.albo.comics.marvel.repository.CreatorRepository;

import org.jboss.logging.Logger;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;

@ApplicationScoped
public class SyncService {

    private static final Logger LOG = Logger.getLogger(SyncService.class);

    @Inject
    private MarvelClientWrapperService marvelClientServiceWrapper;

    @Inject
    private TranslatingService translatingService;

    @Inject
    private CharacterRepository characterRepository;

    @Inject
    private ComicRepository comicRepository;

    @Inject
    private CreatorRepository creatorRepository;

    @CacheResult(cacheName = "query-creators-cache")
    public CharacterCreator getCreatorsAssociatedWithCharacters(String alias) {
        CharacterDO theCharacter = getCharacterByAlias(alias);
        return translatingService.getCreatorsForCharacter(theCharacter);
    }

    @CacheResult(cacheName = "query-characters-cache")
    public CharactersInComicsReponse getCharactersAssociatedWithCharacter(String alias) {
        CharacterDO theCharacter = getCharacterByAlias(alias);
        return translatingService.getCharactersAssociatedWithCharacter(theCharacter);
    }

    private CharacterDO getCharacterByAlias(String alias) {
        return characterRepository.findByAlias(alias);
    }

    public void synchronizeDataFromMarvelApi() {
        List<CharacterDO> characters = characterRepository.find("core", true).list();
        for (CharacterDO characterDO : characters) {
            Character remoteCharacter = marvelClientServiceWrapper.getRemoteCharacterByName(characterDO.getName());
            syncDataByCharacter(remoteCharacter);
        }
        invalidateQueryCaches();
    }

    public void syncDataByCharacter(Character remoteCharacter) {
        LOG.info("Starting sync process from Marvel API");
        Instant start = Instant.now();

        Integer countLimitPerRequest = 100;
        Integer offset = 0;
        Integer comicTotalCount = 0;
        MarvelComicResponse response;

        response = marvelClientServiceWrapper.getComicsByCharacterId(remoteCharacter.getId(), countLimitPerRequest,
                offset);
        comicTotalCount = response.getResponseData().getTotal();
        LOG.debugf("Comic Count for Character %s [%s] = %s", remoteCharacter.getName(), remoteCharacter.getId(),
                comicTotalCount);
         //comicTotalCount = 90; // delete

        processApiResponse(response, remoteCharacter);

        for (int i = 0; i < comicTotalCount / countLimitPerRequest; i++) {
            offset += countLimitPerRequest;
            response = marvelClientServiceWrapper.getComicsByCharacterId(remoteCharacter.getId(), countLimitPerRequest,
                    offset);
            processApiResponse(response, remoteCharacter);
        }

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toSeconds();
        LOG.infof("Sync process from Marvel API Completed in %s seconds", timeElapsed);
    }

    @Transactional
    private void processApiResponse(MarvelComicResponse response, Character remoteCharacter) {
        processCharacterByComicData(response, remoteCharacter);
        processCreatorByComicData(response, remoteCharacter);
    }

    private void processCharacterByComicData(MarvelComicResponse response, Character remoteCharacter) {
        List<Comic> comics = response.getResponseData().getComics();
        for (Comic comic : comics) {
            createAndSaveComic(comic, remoteCharacter.getName());
        }
    }

    private void processCreatorByComicData(MarvelComicResponse response, Character remoteCharacter) {
        List<Comic> comics = response.getResponseData().getComics();
        CharacterDO theCharacter = translatingService.buildCharacter(remoteCharacter, comics);
        characterRepository.save(theCharacter);
    }

    private void createAndSaveComic(Comic comic, String baseCharacterName) {
        ComicDO comicDO = translatingService.buildComic(comic, baseCharacterName);
        comicRepository.save(comicDO);
    }

    @Transactional
    public void deleteComicInfo() {
        long deletedCount = comicRepository.deleteAll();
        LOG.debugf("Deleted Comic count: [ %s ]", deletedCount);
    }

    @Transactional
    public void deleteCreatorsInfo() {
        long deletedCount = creatorRepository.deleteAll();
        LOG.debugf("Deleted Creators count: [ %s ]", deletedCount);
    }

    @Transactional
    public void deleteCharactersInfo() {
        long deletedCount = characterRepository.deleteUnimportant();
        LOG.debugf("Deleted Characters count: [ %s ]", deletedCount);
    }

    @CacheInvalidateAll(cacheName = "query-creators-cache")
    @CacheInvalidateAll(cacheName = "query-characters-cache")
    public void invalidateQueryCaches() {
    }

}

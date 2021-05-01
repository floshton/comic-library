package com.albo.comics.marvel.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.albo.comics.marvel.vo.remote.character.Character;
import com.albo.comics.marvel.vo.remote.comicsByCharacter.Comic;
import com.albo.comics.marvel.vo.remote.comicsByCharacter.MarvelComicResponse;

import org.jboss.logging.Logger;

import com.albo.comics.marvel.domain.CharacterDO;
import com.albo.comics.marvel.domain.ComicDO;
import com.albo.comics.marvel.repository.CharacterRepository;
import com.albo.comics.marvel.repository.ComicRepository;
import com.albo.comics.marvel.repository.CreatorRepository;

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

    private CharacterDO getCharacterWithRemoteDataByName(String name) {
        Character character = marvelClientServiceWrapper.getCharacterByName(name);
        Long idCharacter = character.getId();
        MarvelComicResponse creatorsResponseData = marvelClientServiceWrapper.getComicsByCharacterId(idCharacter);

        CharacterDO theCharacter = translatingService.buildCharacterEntity(character, creatorsResponseData.getResponseData().getComics());
        return theCharacter;
    }

    public CharacterDO getCharacterByAlias(String alias) {
        return characterRepository.findByAlias(alias);
    }

    @Transactional
    public void syncCreatorsByCharacterData() {
        List<CharacterDO> characters = characterRepository.find("core", true).list();
        for (CharacterDO characterDO : characters) {
            CharacterDO character = getCharacterWithRemoteDataByName(characterDO.getName());
            characterRepository.save(character);
        }

    }

    @Transactional
    public void syncCharactersByComicData() {
        Character remoteCharacter = marvelClientServiceWrapper.getRemoteCharacterByAlias("capamerica");
        MarvelComicResponse response = marvelClientServiceWrapper.getComicsByCharacterId(remoteCharacter.getId());
        List<Comic> comics = response.getResponseData().getComics();

        for (Comic comic : comics) {
            Set<Character> charactersInComic = marvelClientServiceWrapper.getCharacterByComic(comic.getId());
            createAndSaveComic(comic, charactersInComic, remoteCharacter.getName());
        }
    }

    private void createAndSaveComic(Comic comic, Set<Character> charactersInComic, String baseCharacterName) {
        ComicDO comicDO = buildComic(comic, charactersInComic, baseCharacterName);
        comicRepository.save(comicDO);
    }

    private ComicDO buildComic(Comic comic, Set<Character> charactersInComic, String baseCharacterName) {
        ComicDO comicDO = new ComicDO(comic.getTitle());
        //comicRepository.save(comicDO);
        Set<CharacterDO> characters = buildCharactersForComic(charactersInComic, baseCharacterName);
        comicDO.setCharacters(characters);
        return comicDO;
    }

    private Set<CharacterDO> buildCharactersForComic(Set<Character> charactersInComic, String baseCharacter) {
        Set<CharacterDO> charactersTemp = charactersInComic.stream().map(item -> new CharacterDO(item.getName()))
                .collect(Collectors.toSet());
        Set<CharacterDO> characters = new HashSet<>();
        for (CharacterDO characterFromApi : charactersTemp) {
            CharacterDO characterDefinite;
            CharacterDO characterFromDb = characterRepository.findByName(characterFromApi.getName());

            if (characterFromDb == null) {
                characterDefinite = characterFromApi;
                characterRepository.save(characterDefinite);
            } else {
                characterDefinite = characterFromDb;
            }
            characters.add(characterDefinite);
        }
        return characters;
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

}

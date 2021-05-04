package com.albo.comics.marvel.service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.albo.comics.marvel.vo.remote.character.Character;
import com.albo.comics.marvel.vo.remote.comicsByCharacter.Comic;
import com.albo.comics.marvel.vo.remote.comicsByCharacter.MarvelComicResponse;
import com.albo.comics.marvel.domain.CharacterDO;
import com.albo.comics.marvel.domain.ComicDO;
import com.albo.comics.marvel.exception.ApiSyncException;
import com.albo.comics.marvel.repository.CharacterRepository;
import com.albo.comics.marvel.repository.ComicRepository;
import com.albo.comics.marvel.repository.CreatorRepository;
import com.albo.comics.marvel.threads.RequestComicForCharacterTask;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.scheduler.Scheduled;

/**
 * This service lists methods for syncing data between Marvel API and local
 * database, providing methods for parsing and transforming response
 */
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

    @ConfigProperty(name = "marvel.api.sync.batch.size")
    private Integer countLimitPerRequest;
    @ConfigProperty(name = "marvel.api.sync.thread.max.count")
    private Integer maxThreadCountAllowed;

    /**
     * Scheduled method for syncing info from Marvel API
     */
    @Scheduled(every = "{marvel.api.sync.frequency}")
    void syncData() {
        try {
            deleteSyncedData();
            synchronizeDataFromMarvelApi();
        } catch (ApiSyncException e) {
            LOG.error("Sync process couldn't be completed");
        }
    }

    /**
     * Sync data from Marvel API for required characters
     */
    public void synchronizeDataFromMarvelApi() throws ApiSyncException {
        LOG.infof("Starting sync process from Marvel API ");
        Instant start = Instant.now();

        try {
            List<CharacterDO> characters = characterRepository.find("core", true).list();

            for (CharacterDO characterDO : characters) {
                Character remoteCharacter = marvelClientServiceWrapper.getRemoteCharacterByName(characterDO.getName());
                syncDataByCharacter(remoteCharacter);
            }

            invalidateQueryCaches();
        } catch (ApiSyncException e) {
            LOG.error("Error while syncing with API", e);
            throw e;
        } finally {
            Instant finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toSeconds();
            LOG.infof("Sync process from Marvel API Completed in [ %s seconds ]", timeElapsed);
        }
    }

    /**
     * Sync data from Marvel API for passed character. Depending on the total
     * results, it might run several times, changing 'offset' parameter in query to
     * API
     * 
     * @param remoteCharacter the instance of Caracter whose data should be
     *                        requested
     */
    public void syncDataByCharacter(Character remoteCharacter) throws ApiSyncException {
        LOG.infof("Starting sync for character %s [ID = %s]", remoteCharacter.getName(), remoteCharacter.getId());

        Integer offset = 0, comicTotalCount = 0;
        MarvelComicResponse response;

        try {
            response = marvelClientServiceWrapper.getComicsByCharacterId(remoteCharacter.getId(), countLimitPerRequest,
                    offset);
            comicTotalCount = response.getResponseData().getTotal();
            //comicTotalCount = (countLimitPerRequest * 3) + 5; // delete

            LOG.infof("Comic Count for Character %s [ ID = %s ] = %s", remoteCharacter.getName(),
                    remoteCharacter.getId(), comicTotalCount);

            processApiResponse(response, remoteCharacter);

            if (comicTotalCount > countLimitPerRequest) {
                LOG.info("Creating threads for additional requests based on total count");
                callWithThreads(remoteCharacter, comicTotalCount);
            }

        } catch (Exception e) {
            LOG.error("Error syncing info from Marvel API Server", e);
            throw new ApiSyncException();
        }
    }

    private Integer getThreadPoolSize(Integer totalRecordsCount) {
        Integer calculatedSize = totalRecordsCount / countLimitPerRequest;
        return Math.min(calculatedSize, maxThreadCountAllowed);
    }

    private void callWithThreads(Character remoteCharacter, Integer totalRecordsCount) {
        Integer maxThreadPoolSize = getThreadPoolSize(totalRecordsCount);
        LOG.debugf("Creating Thread Pool with fixed size of [ %s ]", maxThreadPoolSize);
        ExecutorService executor = Executors.newFixedThreadPool(maxThreadPoolSize);
        List<Future<MarvelComicResponse>> futuresResponse = new ArrayList<>();
        Integer actualNeededThreads = totalRecordsCount / countLimitPerRequest;
        Integer threadOffset = countLimitPerRequest;

        try {
            for (int i = 0; i < actualNeededThreads; i++) {
                threadOffset += countLimitPerRequest;
                Callable task = new RequestComicForCharacterTask(remoteCharacter, marvelClientServiceWrapper,
                        countLimitPerRequest, threadOffset);
                Future<MarvelComicResponse> future = executor.submit(task);
                futuresResponse.add(future);
            }

            for (Future<MarvelComicResponse> future : futuresResponse) {
                MarvelComicResponse response = future.get();
                processApiResponse(response, remoteCharacter);
            }
        } catch (Exception e) {
            LOG.error("Error calling API from executor", e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * 
     * Parses and persists info in DB
     * 
     * @param response        Marvel API response
     * @param remoteCharacter the instance of Caracter whose data was requested
     */
    @Transactional(rollbackOn = { RuntimeException.class, ApiSyncException.class })
    private void processApiResponse(MarvelComicResponse response, Character remoteCharacter) {
        processCharacterByComicData(response, remoteCharacter);
        processCreatorByComicData(response, remoteCharacter);
    }

    private void processCharacterByComicData(MarvelComicResponse response, Character remoteCharacter) {
        LOG.debugf("Processing characters by comic data for Character [%s]", remoteCharacter.getName());
        List<Comic> comics = response.getResponseData().getComics();
        for (Comic comic : comics) {
            createAndSaveComic(comic, remoteCharacter.getName());
        }
    }

    private void processCreatorByComicData(MarvelComicResponse response, Character remoteCharacter) {
        LOG.debugf("Processing creators data for Character [%s]", remoteCharacter.getName());
        List<Comic> comics = response.getResponseData().getComics();
        CharacterDO theCharacter = translatingService.buildCharacter(remoteCharacter, comics);
        characterRepository.save(theCharacter);
    }

    private void createAndSaveComic(Comic comic, String baseCharacterName) {
        ComicDO comicDO = translatingService.buildComic(comic, baseCharacterName);
        comicRepository.save(comicDO);
    }

    /**
     * Delete Comic associated info from DB
     */
    @Transactional
    public void deleteComicInfo() {
        long deletedCount = comicRepository.deleteAll();
        LOG.infof("Deleted Comic count: [ %s ]", deletedCount);
    }

    /**
     * Delete Creators associated info from DB
     */
    @Transactional
    public void deleteCreatorsInfo() {
        long deletedCount = creatorRepository.deleteAll();
        LOG.infof("Deleted Creators count: [ %s ]", deletedCount);
    }

    /**
     * Delete Characters associated info from DB. Note that Captain America and Iron
     * Man, since are flagged as core characters, are not to be deleted
     */
    @Transactional
    public void deleteCharactersInfo() {
        long deletedCount = characterRepository.deleteUnimportant();
        LOG.infof("Deleted Characters count: [ %s ]", deletedCount);
    }

    /**
     * Delete synced info from DB
     */
    private void deleteSyncedData() {
        deleteCharactersInfo();
        deleteComicInfo();
        deleteCreatorsInfo();
    }

    /**
     * Invalidates caches for querying data from DB. Useful after performing a sync,
     * since data might have changed
     */
    @CacheInvalidateAll(cacheName = "query-creators-cache")
    @CacheInvalidateAll(cacheName = "query-characters-cache")
    public void invalidateQueryCaches() {
        LOG.infof("Invalidating caches for keys [ %s ] and [ %s ]", "query-creators-cache", "query-characters-cache");
    }

    /**
     * Invalidates caches for requesting data from API. Useful before a full sync
     */
    @CacheInvalidateAll(cacheName = "api-comics-by-character-cache")
    @CacheInvalidateAll(cacheName = "api-character-name-cache")
    public void invalidateMarvelApiCaches() {
        LOG.infof("Invalidating caches for keys [ %s ] and [ %s ]", "api-comics-by-character-cache",
                "api-character-name-cache");
    }

}

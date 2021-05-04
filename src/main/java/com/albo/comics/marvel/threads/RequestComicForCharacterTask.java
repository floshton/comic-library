package com.albo.comics.marvel.threads;

import java.util.concurrent.Callable;

import com.albo.comics.marvel.vo.remote.character.Character;
import com.albo.comics.marvel.service.MarvelClientWrapperService;
import com.albo.comics.marvel.vo.remote.comicsByCharacter.MarvelComicResponse;

import org.jboss.logging.Logger;

public class RequestComicForCharacterTask implements Callable<MarvelComicResponse> {

    private Character remoteCharacter;
    MarvelClientWrapperService marvelClientServiceWrapper;

    private Integer countLimitPerRequest, offset;

    private static final Logger LOG = Logger.getLogger(RequestComicForCharacterTask.class);

    public RequestComicForCharacterTask(Character remoteCharacter,
            MarvelClientWrapperService marvelClientServiceWrapper, Integer countLimit, Integer offset) {
        this.countLimitPerRequest = countLimit;
        this.marvelClientServiceWrapper = marvelClientServiceWrapper;
        this.offset = offset;
        this.remoteCharacter = remoteCharacter;

    }

    @Override
    public MarvelComicResponse call() throws Exception {
        LOG.debugf("Calling API for parameters: offset = [ %s ], countLimit = [ %s ] ", offset, countLimitPerRequest);
        return marvelClientServiceWrapper.getComicsByCharacterId(remoteCharacter.getId(), countLimitPerRequest, offset);
    }

}

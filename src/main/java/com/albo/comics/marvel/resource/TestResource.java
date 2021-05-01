package com.albo.comics.marvel.resource;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.albo.comics.marvel.domain.CharacterDO;
import com.albo.comics.marvel.domain.CreatorDO;
import com.albo.comics.marvel.domain.CreatorType;
import com.albo.comics.marvel.repository.CharacterRepository;
import com.albo.comics.marvel.repository.CreatorRepository;
import com.albo.comics.marvel.service.SyncService;
import com.albo.comics.marvel.service.TranslatingService;
import com.albo.comics.marvel.vo.local.CharacterCreator;

@Path("/marvel/test")
@Produces(MediaType.APPLICATION_JSON)
public class TestResource {

    @Inject
    private SyncService syncService;

    @Inject
    CreatorRepository creatorRepository;

    @Inject
    CharacterRepository characterRepository;

    public TestResource() {
    }

    @GET
    @Path("/testSyncCreators")
    public String syncCreators() {
        syncService.syncCreatorsByCharacterData();
        return "OK";
    }

    @GET
    @Path("/testSyncCharacterByComic")
    public String getCharactersByComic() {
        syncService.syncCharactersByComicData();
        return "OK";
    }

    @GET
    @Path("/deleteComics")
    public String testDeleteComics() {
        syncService.deleteComicInfo();
        return "OK";
    }

    @GET
    @Path("/deleteCreators")
    public String testDeleteCreators() {
        syncService.deleteCreatorsInfo();
        return "OK";
    }

    @GET
    @Path("/deleteCharacters")
    public String testDeleteCharacters() {
        syncService.deleteCharactersInfo();
        return "OK";
    }
}
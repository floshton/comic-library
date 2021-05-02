package com.albo.comics.marvel.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.albo.comics.marvel.repository.CharacterRepository;
import com.albo.comics.marvel.repository.CreatorRepository;
import com.albo.comics.marvel.service.SyncService;

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
    @Path("/syncData")
    public String syncCreators() {
        syncService.synchronizeDataFromMarvelApi();
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
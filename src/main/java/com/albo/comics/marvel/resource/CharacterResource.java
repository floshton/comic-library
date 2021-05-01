package com.albo.comics.marvel.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.albo.comics.marvel.domain.CharacterDO;
import com.albo.comics.marvel.service.SyncService;
import com.albo.comics.marvel.service.TranslatingService;
import com.albo.comics.marvel.vo.local.CharacterCreator;

@Path("/marvel/characters")
@Produces(MediaType.APPLICATION_JSON)
public class CharacterResource {

    @Inject
    private TranslatingService translatingService;

    @Inject
    private SyncService syncService;

    public CharacterResource() {
    }

    @GET
    @Path("/{alias}")
    public CharacterCreator getComics(@PathParam("alias") String alias) {
        CharacterDO theCharacter = syncService.getCharacterByAlias(alias);
        return translatingService.getCreatorsForCharacter(theCharacter);
    }
}
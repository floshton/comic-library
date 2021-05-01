package com.albo.comics.marvel.resource;

import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.albo.comics.marvel.domain.CharacterDO;
import com.albo.comics.marvel.repository.CharacterRepository;
import com.albo.comics.marvel.service.SyncService;
import com.albo.comics.marvel.service.TranslatingService;
import com.albo.comics.marvel.vo.local.CharacterCreator;

@Path("/marvel/colaborators")
@Produces(MediaType.APPLICATION_JSON)
public class ColaboratorResource {

    @Inject
    private TranslatingService translatingService;

    @Inject
    private SyncService syncService;

    @Inject
    CharacterRepository characterRepository;

    public ColaboratorResource() {
    }

    @GET
    @Path("/{alias}")
    public CharacterCreator getComics(@PathParam("alias") String alias) {
        CharacterDO theCharacter = syncService.getCharacterByAlias(alias);
        return translatingService.getCreatorsForCharacter(theCharacter);
    }
}
package com.albo.comics.marvel.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.albo.comics.marvel.service.QueryService;
import com.albo.comics.marvel.vo.local.CharactersInComicsReponse;

@Path("/marvel/characters")
@Produces(MediaType.APPLICATION_JSON)
public class CharacterResource {

    @Inject
    private QueryService queryService;

    public CharacterResource() {
    }

    @GET
    @Path("/{alias}")
    public CharactersInComicsReponse getComics(@PathParam("alias") String alias) {
        return queryService.getCharactersAssociatedWithCharacter(alias);
    }
}
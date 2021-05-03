package com.albo.comics.marvel.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.albo.comics.marvel.repository.CharacterRepository;
import com.albo.comics.marvel.service.QueryService;
import com.albo.comics.marvel.vo.local.CharacterCreator;

@Path("/marvel/colaborators")
@Produces(MediaType.APPLICATION_JSON)
public class ColaboratorResource {

    @Inject
    private QueryService queryService;

    @Inject
    CharacterRepository characterRepository;

    public ColaboratorResource() {
    }

    @GET
    @Path("/{alias}")
    public CharacterCreator getComics(@PathParam("alias") String alias) throws Exception {
        return queryService.getCreatorsAssociatedWithCharacters(alias);
    }
}
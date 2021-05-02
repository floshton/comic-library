package com.albo.comics.marvel.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.albo.comics.marvel.vo.remote.character.MarvelCharacterResponse;
import com.albo.comics.marvel.vo.remote.comicsByCharacter.MarvelComicResponse;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

@Path("/v1/public")
@RegisterRestClient
public interface MarvelApiClientService {

        @GET
        @Path("/characters")
        @Produces("application/json")
        MarvelCharacterResponse getByName(@QueryParam("name") String name, @QueryParam("ts") String timestamp,
                        @QueryParam("apikey") String apikey, @QueryParam("hash") String hash);

        @GET
        @Path("/characters/{idCharacter}/comics")
        @Produces("application/json")
        MarvelComicResponse getComicsByIdCharacter(@PathParam("idCharacter") Long idCharacter,
                        @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                        @QueryParam("ts") String timestamp, @QueryParam("apikey") String apikey,
                        @QueryParam("hash") String hash);

}

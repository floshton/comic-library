package com.albo.comics.marvel.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.albo.comics.marvel.remote.vo.CharacterResponse;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

@Path("/v1/public")
@RegisterRestClient
public interface MarvelCharacterService {

    @GET
    @Path("/characters")
    @Produces("application/json")
    CharacterResponse getByName(@QueryParam("name") String name, @QueryParam("ts") String timestamp,
            @QueryParam("apikey") String apikey, @QueryParam("hash") String hash);
}

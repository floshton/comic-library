package com.albo.comics.marvel;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.albo.comics.marvel.domain.Character;
import com.albo.comics.marvel.service.ConversionService;
import com.albo.comics.marvel.vo.CharacterCollaborator;

@Path("/characters")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CharacterResource {

    @Inject
    private ConversionService conversionService;

    public CharacterResource() {
    }

    @GET
    public CharacterCollaborator list() {
        Set<String> characters = new HashSet<>();
        characters.add("Captain America");
        characters.add("Iron Man");

        characters.forEach(item -> conversionService.getCharacter(item));

        return conversionService.fromCharacter(new Character("Ironman", 30));
    }
}
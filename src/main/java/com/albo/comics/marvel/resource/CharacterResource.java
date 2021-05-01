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

@Path("/marvel/characters")
@Produces(MediaType.APPLICATION_JSON)
public class CharacterResource {

    @Inject
    private TranslatingService translatingService;

    @Inject
    private SyncService syncService;

    @Inject
    CreatorRepository creatorRepository;

    @Inject
    CharacterRepository characterRepository;

    public CharacterResource() {
    }

    @GET
    @Path("/{name}")
    public CharacterCreator getComics(@PathParam("name") String name) {
        CharacterDO charTemp = characterRepository.findByAlias(name);
        CharacterDO theCharacter = syncService.getCharacterDO(charTemp.getName());
        return translatingService.fromCharacter(theCharacter);
    }

    @GET
    @Path("/testSyncCreators")
    public String syncCreators() {
        syncService.syncCreatorsData();
        return "OK";
    }

    @GET
    @Path("/test")
    public String test() {

        Set<CharacterDO> characters = new HashSet<>();
        CharacterDO character = new CharacterDO("Antman", "antman");
        characters.add(character);

        CreatorDO creator1 = new CreatorDO("Jorge", CreatorType.COLORIST);
        // creator1.setCharacters(characters);
        CreatorDO creator2 = new CreatorDO("Alberto", CreatorType.WRITER);
        // creator2.setCharacters(characters);

        Set<CreatorDO> creators = new HashSet<>();
        creators.add(creator1);
        creators.add(creator2);

        character.setCreators(creators);

        characterRepository.save(character);

        return "OK";
    }
}
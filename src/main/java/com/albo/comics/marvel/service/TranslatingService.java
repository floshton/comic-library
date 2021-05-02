package com.albo.comics.marvel.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.albo.comics.marvel.domain.CharacterDO;
import com.albo.comics.marvel.domain.ComicDO;
import com.albo.comics.marvel.domain.CreatorDO;
import com.albo.comics.marvel.domain.CreatorType;
import com.albo.comics.marvel.repository.CharacterRepository;
import com.albo.comics.marvel.repository.ComicRepository;
import com.albo.comics.marvel.vo.local.CharacterCreator;
import com.albo.comics.marvel.vo.local.CharactersInComicsReponse;
import com.albo.comics.marvel.vo.remote.character.Character;
import com.albo.comics.marvel.vo.remote.comicsByCharacter.Comic;
import com.albo.comics.marvel.vo.remote.comicsByCharacter.ComicPerson;

import org.jboss.logging.Logger;

import io.quarkus.panache.common.Parameters;

@ApplicationScoped
public class TranslatingService {

    @Inject
    CharacterRepository characterRepository;
    @Inject
    ComicRepository comicRepository;

    private static final Logger LOG = Logger.getLogger(TranslatingService.class);

    public CharacterCreator getCreatorsForCharacter(CharacterDO theCharacter) {
        Set<String> writers = getCollaboratorsByType(theCharacter, CreatorType.WRITER);
        Set<String> colorists = getCollaboratorsByType(theCharacter, CreatorType.WRITER);
        Set<String> editors = getCollaboratorsByType(theCharacter, CreatorType.EDITOR);
        LocalDateTime lastSync = theCharacter.getLastSync();

        return new CharacterCreator(lastSync, editors, writers, colorists);
    }

    public CharactersInComicsReponse getCharactersAssociatedWithCharacter(CharacterDO theCharacter) {
        CharactersInComicsReponse theResponse = new CharactersInComicsReponse();
        theResponse.setLastSync(theCharacter.getLastSync());

        List<ComicDO> comics = comicRepository.list(":theCharacter MEMBER OF characters",
                Parameters.with("theCharacter", theCharacter));
        LOG.debugf("Found %s Comic results", comics.size());

        for (ComicDO theComic : comics) {
            List<CharacterDO> charactersInComic = characterRepository
                    .find(":theComics MEMBER OF comics", Parameters.with("theComics", theComic)).list();

            for (CharacterDO character : charactersInComic) {
                theResponse.addCharacterAndComic(character.getName(), theComic.getName());
            }

        }

        return theResponse;
    }

    private Set<String> getCollaboratorsByType(CharacterDO character, CreatorType type) {
        return character.getCreators().stream().filter(col -> col.getType().equals(type)).map(col -> col.getName())
                .sorted().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public ComicDO buildComic(Comic comic, String baseCharacterName) {
        ComicDO comicDO = new ComicDO(comic.getTitle());
        // comicRepository.save(comicDO);
        Set<CharacterDO> characters = buildCharactersForComic(comic, baseCharacterName);
        comicDO.setCharacters(characters);
        return comicDO;
    }

    private Set<CharacterDO> buildCharactersForComic(Comic comic, String baseCharacter) {
        Set<CharacterDO> charactersTemp = comic.getCharactersContainer().getPersons().stream()
                .filter(item -> !baseCharacter.equals(item.getName())).map(item -> new CharacterDO(item.getName()))
                .collect(Collectors.toSet());
        Set<CharacterDO> characters = new HashSet<>();
        for (CharacterDO characterFromApi : charactersTemp) {
            CharacterDO characterDefinite;
            CharacterDO characterFromDb = characterRepository.findByName(characterFromApi.getName());

            if (characterFromDb == null) {
                characterDefinite = characterFromApi;
                characterRepository.save(characterDefinite);
            } else {
                characterDefinite = characterFromDb;
            }
            characters.add(characterDefinite);
        }
        return characters;
    }

    public CharacterDO buildCharacter(Character remoteCharacter, List<Comic> comics) {
        CharacterDO characterFromDb = characterRepository.find("name", remoteCharacter.getName()).firstResult();
        CharacterDO theCharacter;
        if (characterFromDb != null) {
            theCharacter = characterFromDb;
        } else {
            theCharacter = new CharacterDO();
            theCharacter.setName(remoteCharacter.getName());
            theCharacter.setCreated(LocalDateTime.now());
            theCharacter.setLastSync(LocalDateTime.now());
            theCharacter.setCore(false);
            characterRepository.save(theCharacter);
        }

        Set<CreatorDO> creators = comics.stream().flatMap(comic -> comic.getCreatorsContainer().getPersons().stream())
                .filter(creator -> areRolesAllowed(creator))
                .map(creator -> new CreatorDO(creator.getName(), creator.getRole())).collect(Collectors.toSet());

        theCharacter.setCreators(creators);

        return theCharacter;
    }

    private boolean areRolesAllowed(ComicPerson creator) {
        return creator.getRole().equals("editor") || creator.getRole().equals("writer")
                || creator.getRole().equals("colorist");
    }
}

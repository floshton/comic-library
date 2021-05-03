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
import com.albo.comics.marvel.exception.NoDataAvailableException;
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

    /**
     * Transforms a CharacterDO entity into a response for 'colaborators' endpoint
     * 
     * @param theCharacter
     * @return the response that contains collaborators associated with theCharacter
     */
    public CharacterCreator getCreatorsForCharacter(CharacterDO theCharacter) throws NoDataAvailableException {
        Set<String> writers;
        Set<String> colorists;
        Set<String> editors;
        LocalDateTime lastSync = theCharacter.getLastSync();
        try {
            writers = getCollaboratorsByType(theCharacter, CreatorType.WRITER);
            colorists = getCollaboratorsByType(theCharacter, CreatorType.WRITER);
            editors = getCollaboratorsByType(theCharacter, CreatorType.EDITOR);

        } catch (Exception e) {
            LOG.error("Error getting creators data for character [" + theCharacter.getName() + "]", e);
            throw new NoDataAvailableException("No data available");
        }
        return new CharacterCreator(lastSync, editors, writers, colorists);
    }

    /**
     * Builds response object for 'characters' endpoint
     * 
     * @param theCharacter instance of CharacterDO for which associated characters
     *                     should be reported
     * @return the response that contains characters associated with theCharacter
     */
    public CharactersInComicsReponse getCharactersAssociatedWithCharacter(CharacterDO theCharacter)
            throws NoDataAvailableException {
        CharactersInComicsReponse theResponse = new CharactersInComicsReponse();

        try {
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
        } catch (Exception e) {
            LOG.error("Error getting creators data for character [" + theCharacter.getName() + "]", e);
            throw new NoDataAvailableException("No data available");
        }

        return theResponse;
    }

    /**
     * Filter collaborators from a specific type
     * 
     * @param character CharacterDO instance to which collaborators are associated
     * @param type      required type to be filtered
     * @return a Set intance that contains collaborators of specific type for the
     *         character
     */
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

    /**
     * In response, only roles EDITOR, WRITER and COLORIST are required, so others
     * are filtered out
     * 
     * @param creator collaborator with a specific role
     * @return true if the collaborator matches required roles
     */
    private boolean areRolesAllowed(ComicPerson creator) {
        return creator.getRole().equals("editor") || creator.getRole().equals("writer")
                || creator.getRole().equals("colorist");
    }
}

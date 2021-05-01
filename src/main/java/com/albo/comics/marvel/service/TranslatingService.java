package com.albo.comics.marvel.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.albo.comics.marvel.domain.CharacterDO;
import com.albo.comics.marvel.domain.CreatorDO;
import com.albo.comics.marvel.domain.CreatorType;
import com.albo.comics.marvel.repository.CharacterRepository;
import com.albo.comics.marvel.vo.local.CharacterCreator;
import com.albo.comics.marvel.vo.remote.character.Character;
import com.albo.comics.marvel.vo.remote.comicsByCharacter.Comic;
import com.albo.comics.marvel.vo.remote.comicsByCharacter.Creator;

@ApplicationScoped
public class TranslatingService {

    @Inject
    CharacterRepository characterRepository;

    public CharacterCreator getCreatorsForCharacter(CharacterDO character) {
        Set<String> writers = getCollaboratorsByType(character, CreatorType.WRITER);
        Set<String> colorists = getCollaboratorsByType(character, CreatorType.WRITER);
        Set<String> editors = getCollaboratorsByType(character, CreatorType.EDITOR);
        LocalDateTime lastSync = character.getLastSync();

        return new CharacterCreator(lastSync, editors, writers, colorists);
    }

    private Set<String> getCollaboratorsByType(CharacterDO character, CreatorType type) {
        return character.getCreators().stream().filter(col -> col.getType().equals(type)).map(col -> col.getName())
                .collect(Collectors.toSet());
    }

    public CharacterDO buildCharacterEntity(Character remoteCharacter, List<Comic> comics) {

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
        }

        Set<CreatorDO> creators = comics.stream().flatMap(comic -> comic.getCreatorsContainer().getCreators().stream())
                .filter(creator -> areRolesAllowed(creator))
                .map(creator -> new CreatorDO(creator.getName(), creator.getRole())).collect(Collectors.toSet());

        theCharacter.setCreators(creators);

        return theCharacter;
    }

    private boolean areRolesAllowed(Creator creator) {
        return creator.getRole().equals("editor") || creator.getRole().equals("writer")
                || creator.getRole().equals("colorist");
    }

    /*
     * public CharactersInComicsReponse getCharactersInComics(){
     * 
     * }
     */

}

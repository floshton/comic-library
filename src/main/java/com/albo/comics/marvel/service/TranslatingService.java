package com.albo.comics.marvel.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import com.albo.comics.marvel.domain.CharacterDO;
import com.albo.comics.marvel.domain.CreatorDO;
import com.albo.comics.marvel.domain.CreatorType;
import com.albo.comics.marvel.vo.local.CharacterCreator;
import com.albo.comics.marvel.vo.remote.Character;
import com.albo.comics.marvel.vo.remote.Comic;

@ApplicationScoped
public class TranslatingService {

    public CharacterCreator fromCharacter(CharacterDO character) {
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

    public CharacterDO buildCharacter(Character remoteCharacter, List<Comic> comics) {
        CharacterDO theCharacter = new CharacterDO();
        theCharacter.setName(remoteCharacter.getName());
        theCharacter.setCreated(LocalDateTime.now());
        theCharacter.setLastSync(LocalDateTime.now());

        Set<CreatorDO> creators = comics.stream().flatMap(comic -> comic.getCreatorsContainer().getCreators().stream())
                .filter(creator -> creator.getRole().equals("editor") || creator.getRole().equals("writer")
                        || creator.getRole().equals("colorist"))
                .map(creator -> new CreatorDO(creator.getName(), creator.getRole())).collect(Collectors.toSet());

        theCharacter.setCreators(creators);

        return theCharacter;
    }

}

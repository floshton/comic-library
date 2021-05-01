package com.albo.comics.marvel.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.albo.comics.marvel.vo.remote.MarvelComicResponse;
import com.albo.comics.marvel.domain.CharacterDO;
import com.albo.comics.marvel.repository.CharacterRepository;
import com.albo.comics.marvel.vo.remote.Character;

@ApplicationScoped
public class SyncService {

    @Inject
    private MarvelClientWrapperService marvelClientService;

    @Inject
    private TranslatingService translatingService;

    @Inject
    private CharacterRepository characterRepository;

    public CharacterDO getCharacterDO(String name) {
        Character character = marvelClientService.getCharacterByName(name);
        Long idCharacter = character.getId();
        MarvelComicResponse response = marvelClientService.getComicsByCharacterId(idCharacter);

        CharacterDO theCharacter = translatingService.buildCharacter(character, response.getResponseData().getComics());
        return theCharacter;
    }

    public void syncCreatorsData(){
        CharacterDO character = getCharacterDO("Iron Man");
        characterRepository.save(character);
    }

}

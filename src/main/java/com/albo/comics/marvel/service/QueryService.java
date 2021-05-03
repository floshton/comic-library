package com.albo.comics.marvel.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.albo.comics.marvel.domain.CharacterDO;
import com.albo.comics.marvel.exception.InvalidCharacterException;
import com.albo.comics.marvel.exception.NoDataAvailableException;
import com.albo.comics.marvel.repository.CharacterRepository;
import com.albo.comics.marvel.vo.local.CharacterCreator;
import com.albo.comics.marvel.vo.local.CharactersInComicsReponse;

import io.quarkus.cache.CacheResult;

@ApplicationScoped
public class QueryService {

    @Inject
    private TranslatingService translatingService;

    @Inject
    private CharacterRepository characterRepository;

    @CacheResult(cacheName = "query-creators-cache")
    public CharacterCreator getCreatorsAssociatedWithCharacters(String alias) throws Exception {
        CharacterDO theCharacter = getCharacterByAlias(alias);
        return translatingService.getCreatorsForCharacter(theCharacter);
    }

    @CacheResult(cacheName = "query-characters-cache")
    public CharactersInComicsReponse getCharactersAssociatedWithCharacter(String alias) throws Exception {
        CharacterDO theCharacter = getCharacterByAlias(alias);
        CharactersInComicsReponse response = translatingService.getCharactersAssociatedWithCharacter(theCharacter);
        if (response == null || response.getCharactersAndComics() == null) {
            throw new NoDataAvailableException("No hay datos disponibles");
        }
        return response;

    }

    private CharacterDO getCharacterByAlias(String alias) throws Exception {
        CharacterDO character = characterRepository.findByAlias(alias);
        if (character == null) {
            throw new InvalidCharacterException("El personaje '" + alias + "' no existe en la base de datos.");
        }
        return character;
    }

}

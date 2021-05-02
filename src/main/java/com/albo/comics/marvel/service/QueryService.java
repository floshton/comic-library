package com.albo.comics.marvel.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.albo.comics.marvel.domain.CharacterDO;
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
    public CharacterCreator getCreatorsAssociatedWithCharacters(String alias) {
        CharacterDO theCharacter = getCharacterByAlias(alias);
        return translatingService.getCreatorsForCharacter(theCharacter);
    }

    @CacheResult(cacheName = "query-characters-cache")
    public CharactersInComicsReponse getCharactersAssociatedWithCharacter(String alias) {
        CharacterDO theCharacter = getCharacterByAlias(alias);
        return translatingService.getCharactersAssociatedWithCharacter(theCharacter);
    }

    private CharacterDO getCharacterByAlias(String alias) {
        return characterRepository.findByAlias(alias);
    }

}

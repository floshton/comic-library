package com.albo.comics.marvel.service;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.albo.comics.marvel.domain.CharacterDO;
import com.albo.comics.marvel.repository.CharacterRepository;
import com.albo.comics.marvel.vo.remote.character.Character;
import com.albo.comics.marvel.vo.remote.character.MarvelCharacterResponse;
import com.albo.comics.marvel.vo.remote.charactersByComic.MarvelCharactersByComicResponse;
import com.albo.comics.marvel.vo.remote.comicsByCharacter.MarvelComicResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import io.quarkus.cache.CacheResult;

@ApplicationScoped
public class MarvelClientWrapperService {

    private static final Logger LOG = Logger.getLogger(MarvelClientWrapperService.class);

    @Inject
    @RestClient
    MarvelApiClientService marvelCharacterService;

    @Inject
    CharacterRepository characterRepository;

    @ConfigProperty(name = "marvel.api.key.public")
    private String publicKey;

    @ConfigProperty(name = "marvel.api.key.private")
    private String privateKey;

    private String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    private String getHash(String timeStamp) {
        StringBuilder apiKeys = new StringBuilder(timeStamp);
        apiKeys.append(privateKey);
        apiKeys.append(publicKey);

        String theHash = DigestUtils.md5Hex(apiKeys.toString());
        return theHash;
    }

    @CacheResult(cacheName = "comics-by-character-cache")
    public MarvelComicResponse getComicsByCharacterId(Long id, Integer limit, Integer offset) {
        String ts = getTimeStamp();
        LOG.infof("Requesting Comic data for character with id %s. Limit = [%s]. Offset = [%s]", id, limit, offset);
        MarvelComicResponse response = marvelCharacterService.getComicsByIdCharacter(id, limit, offset, ts, publicKey, getHash(ts));
        return response;
    }

    @CacheResult(cacheName = "character-name-cache")
    public Character getRemoteCharacterByName(String name) {
        Character theCharacter = null;
        String ts = getTimeStamp();
        LOG.infof("Requesting Character data for character %s", name);
        MarvelCharacterResponse response = marvelCharacterService.getByName(name, ts, publicKey, getHash(ts));

        if (response != null && "200".equals(response.getCode())) {
            theCharacter = (Character) response.getResponseData().getCharacters().toArray()[0];
        }
        return theCharacter;
    }

    // @CacheResult(cacheName = "characters-by-comic-cache")
    public Set<Character> getCharacterByComic(Long id) {
        String ts = getTimeStamp();
        LOG.infof("Requesting Character data for Comic %s", id);
        MarvelCharactersByComicResponse response = marvelCharacterService.getCharactersByComicId(id, ts, publicKey,
                getHash(ts));

        return response.getResponseData().getCharacters();
    }

    @CacheResult(cacheName = "character-alias-cache")
    public Character getRemoteCharacterByAlias(String alias) {
        CharacterDO character = characterRepository.findByAlias(alias);
        return this.getRemoteCharacterByName(character.getName());
    }

}

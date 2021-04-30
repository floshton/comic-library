package com.albo.comics.marvel.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.albo.comics.marvel.vo.remote.Character;
import com.albo.comics.marvel.vo.remote.MarvelCharacterResponse;
import com.albo.comics.marvel.vo.remote.MarvelComicResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MarvelClientWrapperService {

    private static final Logger LOG = Logger.getLogger(MarvelClientWrapperService.class);

    @Inject
    @RestClient
    MarvelApiClientService marvelCharacterService;

    private String publicKey = "ac1e13c77e75cf1367b4428c60bf1451";
    private String privateKey = "d22738b7c1293d0ba6bbd71e77f1defaceb4ac51";

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

    public MarvelComicResponse getComicsByCharacterId(Long id) {
        String ts = getTimeStamp();
        MarvelComicResponse response = marvelCharacterService.getComicsByIdCharacter(id, ts, publicKey, getHash(ts));
        return response;
    }

    public Character getCharacterByName(String name) {
        Character theCharacter = null;
        String ts = getTimeStamp();
        MarvelCharacterResponse response = marvelCharacterService.getByName(name, ts, publicKey, getHash(ts));

        LOG.debugf("Performing query for character %s ", name);

        if (response != null && "200".equals(response.getCode())) {
            theCharacter = response.getResponseData().getCharacters().get(0);
        }
        return theCharacter;
    }

}

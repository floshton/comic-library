package com.albo.comics.marvel.service;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.albo.comics.marvel.domain.Character;
import com.albo.comics.marvel.domain.CollaboratorType;
import com.albo.comics.marvel.remote.vo.CharacterResponse;
import com.albo.comics.marvel.vo.CharacterCollaborator;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class ConversionService {

    @Inject
    @RestClient
    MarvelCharacterService marvelCharacterService;

    public CharacterCollaborator fromCharacter(Character character) {
        Set<String> writers = getCollaboratorsByType(character, CollaboratorType.WRITER);
        Set<String> colorists = getCollaboratorsByType(character, CollaboratorType.WRITER);
        Set<String> editors = getCollaboratorsByType(character, CollaboratorType.EDITOR);
        Date lastSync = new Date();

        return new CharacterCollaborator(lastSync, editors, writers, colorists);
    }

    private Set<String> getCollaboratorsByType(Character character, CollaboratorType type) {
        return character.collaborators.stream().filter(col -> col.type.equals(type)).map(col -> col.name)
                .collect(Collectors.toSet());
    }

    /* public void getData() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getUrl());
        Response response = target.request().get();
        String value = response.readEntity(String.class);
        response.close();

        System.out.println(value);
    } */

    public void getCharacter(String characterName) {
        //String baseUrl = "https://gateway.marvel.com:443/v1/public/characters?name=Captain%20America";
        String publicKey = "ac1e13c77e75cf1367b4428c60bf1451";
        String privateKey = "d22738b7c1293d0ba6bbd71e77f1defaceb4ac51";
        Long ts = System.currentTimeMillis();

        StringBuilder theString = new StringBuilder(ts.toString());
        theString.append(privateKey);
        theString.append(publicKey);

        String hash = DigestUtils.md5Hex(theString.toString());

        CharacterResponse response = marvelCharacterService.getByName(characterName, ts.toString(), publicKey, hash);
        System.out.println(response);
    }
}

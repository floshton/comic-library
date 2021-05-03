package com.albo.comics.marvel.vo.local;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.albo.comics.marvel.exception.NoDataAvailableException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents response to 'characters' endpoint. Provides utility
 * methods for appending info to existing instance and formatting retrieval
 */
public class CharactersInComicsReponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyy hh:mm:ss")
    @JsonProperty("last_sync")
    private LocalDateTime lastSync;
    @JsonIgnore
    private Map<String, Set<String>> charactersAndComics;

    public CharactersInComicsReponse() {
    }

    public Map<String, Set<String>> getCharactersAndComics() {
        return charactersAndComics;
    }

    public void setCharactersAndComics(Map<String, Set<String>> charactersAndComics) {
        this.charactersAndComics = charactersAndComics;
    }

    public LocalDateTime getLastSync() {
        return lastSync;
    }

    public void setLastSync(LocalDateTime lastSync) {
        this.lastSync = lastSync;
    }

    public CharactersInComicsReponse(LocalDateTime lastSync, Map<String, Set<String>> charactersAndComics) {
        this.lastSync = lastSync;
        this.charactersAndComics = charactersAndComics;
    }

    public void addCharacterAndComic(String newCharacterName, String newComicName) {
        if (charactersAndComics == null) {
            charactersAndComics = new HashMap<>();
        }

        if (!charactersAndComics.containsKey(newCharacterName)) {
            addNewCharacterToMap(newCharacterName);
        }

        addNewComicToExistingCharacter(newCharacterName, newComicName);
    }

    private void addNewCharacterToMap(String newCharacter) {
        Set<String> comicSet = new HashSet<>();
        charactersAndComics.put(newCharacter, comicSet);
    }

    private void addNewComicToExistingCharacter(String newCharacterName, String newComicName) {
        if (charactersAndComics.containsKey(newCharacterName) && charactersAndComics.get(newCharacterName) != null) {
            charactersAndComics.get(newCharacterName).add(newComicName);
        }
    }

    @JsonGetter("characters")
    public List<CharactersComicInfo> getCharacters() {
        List<CharactersComicInfo> characters = charactersAndComics.entrySet().stream()
                .map(item -> new CharactersComicInfo(item.getKey(), item.getValue())).collect(Collectors.toList());

        return characters;
    }

}

package com.albo.comics.marvel.vo.remote.character;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CharacterResponseData {

    @JsonProperty("results")
    private Set<Character> characters;

    public Set<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(Set<Character> characters) {
        this.characters = characters;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        characters.forEach(item -> string.append(item));

        return string.toString();
    }

}

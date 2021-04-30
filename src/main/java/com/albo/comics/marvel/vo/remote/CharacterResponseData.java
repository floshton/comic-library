package com.albo.comics.marvel.vo.remote;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CharacterResponseData {

    @JsonProperty("results")
    private List<Character> characters;

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        characters.forEach(item -> string.append(item));

        return string.toString();
    }

}

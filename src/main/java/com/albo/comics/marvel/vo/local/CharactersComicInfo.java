package com.albo.comics.marvel.vo.local;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CharactersComicInfo {

    private String character;
    @JsonProperty("Comics")
    private Set<String> comics;

    public CharactersComicInfo() {
        comics = new HashSet<>();
    }

    public CharactersComicInfo(String character, Set<String> comics) {
        this.character = character;
        this.comics = comics;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public Set<String> getComics() {
        return comics;
    }

    public void setComics(Set<String> comics) {
        this.comics = comics;
    }

    public void addComic(String newComic) {
        if (comics == null) {
            comics = new HashSet<>();
        }
        comics.add(newComic);
    }
}

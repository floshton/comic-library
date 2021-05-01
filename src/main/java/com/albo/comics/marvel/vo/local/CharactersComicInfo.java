package com.albo.comics.marvel.vo.local;

import java.util.Set;

public class CharactersComicInfo {

    public String character;
    public Set<String> comics;

    public CharactersComicInfo() {
    }

    public CharactersComicInfo(String character, Set<String> comics) {
        this.character = character;
        this.comics = comics;
    }

}

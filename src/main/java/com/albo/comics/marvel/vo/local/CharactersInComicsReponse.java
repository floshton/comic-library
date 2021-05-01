package com.albo.comics.marvel.vo.local;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CharactersInComicsReponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyy hh:mm:ss")
    @JsonProperty("last_sync")
    public LocalDateTime lastSync;
    public Set<CharactersComicInfo> characters;

    public CharactersInComicsReponse() {

    }

    public CharactersInComicsReponse(LocalDateTime lastSync, Set<CharactersComicInfo> characters) {
        this.lastSync = lastSync;
        this.characters = characters;
    }

}

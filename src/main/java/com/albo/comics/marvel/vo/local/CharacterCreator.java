package com.albo.comics.marvel.vo.local;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CharacterCreator {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyy hh:mm:ss")
    @JsonProperty("last_sync")
    public LocalDateTime lastSync;
    public Set<String> editors;
    public Set<String> writers;
    public Set<String> colorists;

    public CharacterCreator() {

    }

    public CharacterCreator(LocalDateTime lastSync, Set<String> editors, Set<String> writers, Set<String> colorists) {
        this.lastSync = lastSync;
        this.editors = editors;
        this.writers = writers;
        this.colorists = colorists;
    }

}

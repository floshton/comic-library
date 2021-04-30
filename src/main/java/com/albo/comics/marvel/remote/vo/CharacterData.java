package com.albo.comics.marvel.remote.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CharacterData {

    public Long id;
    public String name;

}

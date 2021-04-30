package com.albo.comics.marvel.remote.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CharacterResponseData {

    @JsonProperty("results")
    public List<CharacterData> characters;

}

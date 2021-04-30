package com.albo.comics.marvel.remote.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CharacterResponse {

    public String code;
    public String status;
    @JsonProperty("data")
    public CharacterResponseData responseData;

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("code: ").append(code);
        string.append("status: ").append(status);
        /* string.append("character id: ").append(responseData.character.id);
        string.append("character name: ").append(responseData.character.name); */

        return string.toString();
    }
}

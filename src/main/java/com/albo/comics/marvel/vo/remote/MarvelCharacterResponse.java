package com.albo.comics.marvel.vo.remote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarvelCharacterResponse {

    private String code;
    private String status;
    @JsonProperty("data")
    private CharacterResponseData responseData;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CharacterResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(CharacterResponseData responseData) {
        this.responseData = responseData;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("code: ").append(code);
        string.append(", status: ").append(status);
        string.append(", responseData: ").append(responseData);
        return string.toString();
    }
}

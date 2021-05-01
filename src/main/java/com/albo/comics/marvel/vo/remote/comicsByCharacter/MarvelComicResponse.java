package com.albo.comics.marvel.vo.remote.comicsByCharacter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarvelComicResponse {

    private String code;
    private String status;
    @JsonProperty("data")
    private ComicResponseData responseData;

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

    public ComicResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ComicResponseData responseData) {
        this.responseData = responseData;
    }

}

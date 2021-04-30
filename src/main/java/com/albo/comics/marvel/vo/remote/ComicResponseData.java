package com.albo.comics.marvel.vo.remote;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ComicResponseData {

    @JsonProperty("results")
    private List<Comic> comics;

    public List<Comic> getComics() {
        return comics;
    }

    public void setComics(List<Comic> comics) {
        this.comics = comics;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        comics.forEach(item -> string.append(item));

        return string.toString();
    }

}

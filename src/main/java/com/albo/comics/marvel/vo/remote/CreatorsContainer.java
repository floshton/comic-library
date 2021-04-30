package com.albo.comics.marvel.vo.remote;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreatorsContainer {

    @JsonProperty("items")
    private List<Creator> creators;

    public List<Creator> getCreators() {
        return creators;
    }

    public void setCreators(List<Creator> creators) {
        this.creators = creators;
    }

}

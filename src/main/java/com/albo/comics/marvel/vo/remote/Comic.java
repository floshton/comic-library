package com.albo.comics.marvel.vo.remote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Comic {

    private Long id;
    private String title;
    @JsonProperty("creators")
    private CreatorsContainer creatorsContainer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CreatorsContainer getCreatorsContainer() {
        return creatorsContainer;
    }

    public void setCreatorsContainer(CreatorsContainer creatorsContainer) {
        this.creatorsContainer = creatorsContainer;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("id: ").append(id);
        string.append(", name: ").append(title);

        return string.toString();
    }

}

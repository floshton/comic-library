package com.albo.comics.marvel.vo.remote.character;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Character {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("id: ").append(id);
        string.append(", name: ").append(name);

        return string.toString();
    }

}

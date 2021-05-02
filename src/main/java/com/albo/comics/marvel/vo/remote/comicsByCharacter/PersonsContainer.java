package com.albo.comics.marvel.vo.remote.comicsByCharacter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonsContainer {

    @JsonProperty("items")
    private List<ComicPerson> persons;

    public List<ComicPerson> getPersons() {
        return persons;
    }

    public void setPersons(List<ComicPerson> persons) {
        this.persons = persons;
    }
}

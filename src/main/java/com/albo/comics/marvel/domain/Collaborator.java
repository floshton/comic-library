package com.albo.comics.marvel.domain;

public class Collaborator {
    public String name;
    public Long id;
    public CollaboratorType type;

    public Collaborator() {

    }

    public Collaborator(Long id, String name, CollaboratorType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}
package com.albo.comics.marvel.domain;

public class CreatorDO {
    public String name;
    public Long id;
    public CreatorType type;

    public CreatorDO() {

    }

    public CreatorDO(String name, CreatorType type) {
        this.name = name;
        this.type = type;
    }

    public CreatorDO(String name, String type){
        this.name = name;
        this.type = CreatorType.fromString(type);
    }
}
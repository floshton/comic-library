package com.albo.comics.marvel.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;

@Entity(name = "comic")
public class ComicDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_comic")
    private Long id;
    private String name;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "character_comic", joinColumns = @JoinColumn(name = "pk_comic"), inverseJoinColumns = @JoinColumn(name = "pk_character"))
    Set<CharacterDO> characters;

    public ComicDO() {
    }

    public ComicDO(String name) {
        this.name = name;
    }

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

    public Set<CharacterDO> getCharacters() {
        return characters;
    }

    public void setCharacters(Set<CharacterDO> characters) {
        this.characters = characters;
    }
}

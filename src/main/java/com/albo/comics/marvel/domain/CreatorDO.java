package com.albo.comics.marvel.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity(name = "creator")
public class CreatorDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_creator")
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "creator_type")
    private CreatorType type;

    @ManyToMany(mappedBy = "creators")
    Set<CharacterDO> characters;

    public CreatorDO() {
    }

    public CreatorDO(String name, CreatorType type) {
        this.name = name;
        this.type = type;
    }

    public CreatorDO(String name, String type) {
        this.name = name;
        this.type = CreatorType.fromString(type);
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

    public CreatorType getType() {
        return type;
    }

    public void setType(CreatorType type) {
        this.type = type;
    }

    public Set<CharacterDO> getCharacters() {
        return characters;
    }

    public void setCharacters(Set<CharacterDO> characters) {
        this.characters = characters;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof CreatorDO) {
            CreatorDO other = (CreatorDO) obj;
            return this.name.equals(other.getName()) && this.type.equals(other.getType());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        final long prime = 31L;
        long result = 1L;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return (int) result;
    }
}
package com.albo.comics.marvel.domain;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import javax.persistence.JoinColumn;

@Entity(name = "personaje")
public class CharacterDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_character")
    private Long id;
    private String name;
    private String alias;
    @Column(name = "dt_created")
    private LocalDateTime created;
    @Column(name = "dt_last_sync")
    private LocalDateTime lastSync;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "character_creator", joinColumns = @JoinColumn(name = "pk_character"), inverseJoinColumns = @JoinColumn(name = "pk_creator"))
    private Set<CreatorDO> creators;

    public CharacterDO() {
    }

    public CharacterDO(String name, String alias) {
        this.name = name;
        this.alias = alias;
        this.created = LocalDateTime.now();
        this.lastSync = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CreatorDO> getCreators() {
        return creators;
    }

    public void setCreators(Set<CreatorDO> creators) {
        this.creators = creators;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLastSync() {
        return lastSync;
    }

    public void setLastSync(LocalDateTime lastSync) {
        this.lastSync = lastSync;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}

package com.albo.comics.marvel.repository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import com.albo.comics.marvel.domain.CharacterDO;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class CharacterRepository implements PanacheRepository<CharacterDO> {

    @Transactional
    public void save(CharacterDO entity) {
        try {
            persistAndFlush(entity);
        } catch (PersistenceException pe) {
            System.out.println(pe);
            // LOG.error("Unable to create the parameter", pe);
        }
    }

    public CharacterDO findByAlias(String alias) {
        return find("alias", alias).firstResult();
    }
}
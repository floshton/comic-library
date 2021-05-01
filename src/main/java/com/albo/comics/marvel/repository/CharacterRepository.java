package com.albo.comics.marvel.repository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import com.albo.comics.marvel.domain.CharacterDO;

import org.jboss.logging.Logger;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class CharacterRepository implements PanacheRepository<CharacterDO> {

    private static final Logger LOG = Logger.getLogger(CharacterRepository.class);

    @Transactional
    public void save(CharacterDO entity) {
        try {
            if (entity.getId() != null) {
                getEntityManager().merge(entity);
            }
            persistAndFlush(entity);
        } catch (PersistenceException pe) {
            LOG.errorf("Unable to add Character with name [ %s ] to DB. Detail: %s", entity.getName(), pe);
        }
    }

    public CharacterDO findByAlias(String alias) {
        return find("alias", alias).firstResult();
    }

    public CharacterDO findByName(String name) {
        return find("name", name).firstResult();
    }

    public long deleteUnimportant() {
        return delete("core", false);
    }
}
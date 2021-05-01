package com.albo.comics.marvel.repository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import com.albo.comics.marvel.domain.CreatorDO;

import org.jboss.logging.Logger;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class CreatorRepository implements PanacheRepository<CreatorDO> {

    private static final Logger LOG = Logger.getLogger(CreatorRepository.class);

    @Transactional
    public void save(CreatorDO entity) {
        try {
            if (entity.getId() != null) {
                getEntityManager().merge(entity);
            }
            persistAndFlush(entity);
        } catch (PersistenceException pe) {
            LOG.errorf("Unable to add Creator with name [ %s ] to DB. Detail: %s", entity.getName(), pe.getCause());
        }
    }

}
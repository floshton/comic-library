package com.albo.comics.marvel.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import com.albo.comics.marvel.domain.ComicDO;

import org.jboss.logging.Logger;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ComicRepository implements PanacheRepository<ComicDO> {

    private static final Logger LOG = Logger.getLogger(ComicRepository.class);

    @Transactional
    public void save(ComicDO entity) {
        try {
            if(entity.getId() != null){
                getEntityManager().merge(entity);
            }
            persistAndFlush(entity);
        } catch (PersistenceException pe) {
            LOG.errorf("Unable to add Comic with name [ %s ] to DB. Detail: %s", entity.getName(), pe);
        }
    }

/*     public void deleteAllComic(){
        try {
            deleteAll();
            List<ComicDO> comics = listAll();
            comics.forEach(comic -> delete(comic));
        } catch (Exception e) {
            //TODO: handle exception
        }
    } */

}
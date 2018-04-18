package org.ivanina.course.spring37.cinema.dao;

import org.ivanina.course.spring37.cinema.domain.DomainObject;
import org.ivanina.course.spring37.cinema.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public abstract class DomainStore<T extends DomainObject> extends SimpleStorage<T> implements Dao<T> {

    private Dao dao;
    public void setDao(Dao dao){
        this.dao = dao;
    }
    public Dao getDao() {
        return this.dao;
    }

    @Override
    public Set<T> getAll() {
        // return new HashSet<T>(store.values());
        return dao.getAll();
    }

    @Override
    public T get(Long id) {
        return store.get(id);
    }

    @Override
    public Long save(T entity) {
        if(entity.getId() == null){
            entity.setId( getNextIncrement() );
        }
        store.put(  entity.getId(), entity );
        return entity.getId();
    }


    @Override
    public Boolean remove(T entity) {
        if(store.get( entity.getId()) == null) return false;
        store.remove( entity.getId() );
        return true;
    }

    @Override
    public Boolean remove(Long id) {
        if(store.get( id ) == null) return false;
        return remove(store.get( id ));
    }

    @Override
    public Long getNextIncrement() {
        return store.size() == 0 ? 1L : store.keySet().stream().max( Long::compareTo ).orElse(0L) + 1L;
    }
}

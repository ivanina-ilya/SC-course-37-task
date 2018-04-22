package org.ivanina.course.spring37.cinema.dao;

import java.util.Set;


public interface Dao<T> {
    Set<T> getAll();

    T get(Long id);

    Long save(T entity);

    Boolean remove(T entity);

    Boolean remove(Long id);

    Long getNextIncrement();

}

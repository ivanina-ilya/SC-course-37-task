package org.ivanina.course.spring37.cinema.dao;

import org.ivanina.course.spring37.cinema.domain.Counter;

import java.util.Map;

public interface CounterDao extends Dao<Counter> {
    Counter get(String key);

    Map<String, Long> getMap();
}

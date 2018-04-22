package org.ivanina.course.spring37.cinema.service;

import org.ivanina.course.spring37.cinema.domain.Counter;

import java.util.Map;
import java.util.Set;

public interface CounterService {

    Map<String, Long> getMap();

    Set<Counter> getAll();

    Long getCount(String key);

    Long add(String key);

    Long add(String key, Long cnt);

    Counter getCounter(String key);
}

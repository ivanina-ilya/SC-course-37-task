package org.ivanina.course.spring37.cinema.service.impl;

import org.ivanina.course.spring37.cinema.dao.CounterDao;
import org.ivanina.course.spring37.cinema.domain.Counter;
import org.ivanina.course.spring37.cinema.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;
import java.util.Set;

public class CounterServiceImpl implements CounterService {

    @Autowired
    @Qualifier("counterDao")
    private CounterDao counterDao;

    @Override
    public Map<String, Long> getMap() {
        return counterDao.getMap();
    }

    @Override
    public Set<Counter> getAll() {
        return counterDao.getAll();
    }

    @Override
    public Counter getCounter(String key) {
        return counterDao.get(key);
    }

    @Override
    public Long getCount(String key) {
        Counter counter = counterDao.get(key);
        return counter != null ? counter.getCount() : 0L;
    }

    @Override
    public Long add(String key) {
        return add(key, 1L);
    }

    @Override
    public Long add(String key, Long cnt) {
        Counter counter = counterDao.get(key);
        if (counter == null) counter = new Counter(null, key, 0L);
        counter.add(cnt);
        return counterDao.save(counter);
    }
}

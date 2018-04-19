package org.ivanina.course.spring37.cinema.service.impl;

import org.ivanina.course.spring37.cinema.dao.AuditoriumDao;
import org.ivanina.course.spring37.cinema.dao.DomainStore;
import org.ivanina.course.spring37.cinema.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.ivanina.course.spring37.cinema.domain.Auditorium;
import org.ivanina.course.spring37.cinema.service.AuditoriumService;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

public class AuditoriumServiceImpl implements AuditoriumService {

    @Autowired
    @Qualifier("auditoriumDao")
    private AuditoriumDao auditoriumDao;

    @Nullable
    @Override
    public Auditorium getByName(String name) {
        return auditoriumDao.getByName(name);
    }

    @Override
    public Set<Auditorium> getAll() {
        return auditoriumDao.getAll();
    }

    @Override
    public Auditorium get(Long id) {
        return auditoriumDao.get(id);
    }

    @Override
    public Long save(Auditorium entity) {
        return auditoriumDao.save(entity);
    }

    @Override
    public Boolean remove(Auditorium entity) {
        return auditoriumDao.remove(entity);
    }

    @Override
    public Boolean remove(Long id) {
        return auditoriumDao.remove(id);
    }

    @Override
    public Long getNextIncrement() {
        return null;
    }
}

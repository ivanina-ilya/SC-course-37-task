package org.ivanina.course.spring37.cinema.service.impl;

import org.ivanina.course.spring37.cinema.dao.EventDao;
import org.ivanina.course.spring37.cinema.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.ivanina.course.spring37.cinema.dao.DomainStore;
import org.ivanina.course.spring37.cinema.domain.Event;
import org.ivanina.course.spring37.cinema.domain.EventRating;
import org.ivanina.course.spring37.cinema.service.AuditoriumService;
import org.ivanina.course.spring37.cinema.service.EventService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class EventServiceImpl  implements EventService {

    @Autowired
    @Qualifier("eventDao")
    private EventDao eventDao;



    @Nullable
    @Override
    public Event getByName(String name) {
        return eventDao.getByName(name);
    }

    @Override
    public Set<Event> getForDateRange(LocalDateTime from, LocalDateTime to) {
        // TODO: to be refactored! Without getAll
        return eventDao.getAll().stream()
                .filter( event -> event.getAirDates().stream()
                        .anyMatch( air -> air.isAfter(from) || air.isEqual(from) ) )
                .filter( event -> event.getAirDates().stream()
                        .anyMatch( air -> air.isBefore(to) || air.isEqual(to) ) )
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Event> getNextEvents(LocalDateTime to) {
        return getForDateRange( LocalDateTime.now(), to );
    }

    @Override
    public Set<Event> getAvailableEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventDao.getAll().stream()
                .filter( event -> event.getAirDates().stream()
                        .anyMatch( air -> air.isAfter(now)  ) )
                .collect(Collectors.toSet());
    }

    @Override
    public Set<LocalDateTime> getAvailableEventDate(Event event) {
        LocalDateTime now = LocalDateTime.now();
        return event.getAirDates().stream()
                .filter( air -> air.isAfter(now))
                .collect(Collectors.toSet());
    }

    @Override
    public Event getNewEvent(String name) {
        return new Event(name);
    }


    @Override
    public Set<Event> getAll() {
        return eventDao.getAll();
    }

    @Override
    public Event get(Long id) {
        return eventDao.get(id);
    }

    @Override
    public Long save(Event entity) {
        return eventDao.save(entity);
    }

    @Override
    public Boolean remove(Event entity) {
        return eventDao.remove(entity);
    }

    @Override
    public Boolean remove(Long id) {
        return eventDao.remove(id);
    }

    @Override
    public Long getNextIncrement() {
        return null;
    }
}

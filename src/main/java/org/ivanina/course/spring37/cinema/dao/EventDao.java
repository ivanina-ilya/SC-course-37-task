package org.ivanina.course.spring37.cinema.dao;

import org.ivanina.course.spring37.cinema.domain.Auditorium;
import org.ivanina.course.spring37.cinema.domain.Event;

import java.time.LocalDateTime;
import java.util.NavigableMap;

public interface EventDao extends Dao<Event> {
    Event getByName(String name);
    NavigableMap<LocalDateTime, Auditorium> getEventAuditoriums(Long eventId);
}

package org.ivanina.course.spring37.cinema.dao;

import org.ivanina.course.spring37.cinema.domain.Event;

public interface EventDao extends Dao<Event> {
    Event getByName(String name);
}

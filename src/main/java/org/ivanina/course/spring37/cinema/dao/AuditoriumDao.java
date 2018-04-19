package org.ivanina.course.spring37.cinema.dao;

import org.ivanina.course.spring37.cinema.domain.Auditorium;

public interface AuditoriumDao extends Dao<Auditorium> {
    Auditorium getByName(String name);
}

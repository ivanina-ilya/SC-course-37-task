package org.ivanina.course.spring37.cinema.dao;

import org.ivanina.course.spring37.cinema.domain.Event;
import org.ivanina.course.spring37.cinema.domain.Ticket;

import java.util.NavigableSet;

public interface TicketDao extends Dao<Ticket> {
    NavigableSet<Ticket> getTicketsByUser(Long userId);
}

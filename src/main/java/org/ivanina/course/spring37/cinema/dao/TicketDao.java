package org.ivanina.course.spring37.cinema.dao;

import org.ivanina.course.spring37.cinema.domain.Ticket;

import java.time.LocalDateTime;
import java.util.NavigableSet;

public interface TicketDao extends Dao<Ticket> {
    NavigableSet<Ticket> getTicketsByUser(Long userId);

    NavigableSet<Ticket> getTicketsByEvent(Long eventId, LocalDateTime dateTime);

    NavigableSet<Ticket> getTicketsByUserForEvent(Long userId, Long eventId);

    NavigableSet<Ticket> getTicketsByUserForEvent(Long userId, Long eventId, LocalDateTime dateTime);
}

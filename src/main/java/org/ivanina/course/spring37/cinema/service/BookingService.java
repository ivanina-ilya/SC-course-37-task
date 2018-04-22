package org.ivanina.course.spring37.cinema.service;

import org.ivanina.course.spring37.cinema.domain.Event;
import org.ivanina.course.spring37.cinema.domain.Ticket;
import org.ivanina.course.spring37.cinema.domain.User;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public interface BookingService {
    BigDecimal getTicketsPrice(@NonNull Event event,
                               @NonNull LocalDateTime dateTime,
                               @Nullable User user,
                               @NonNull Set<Long> seats);

    void bookTickets(@NonNull Set<Ticket> tickets);

    void bookTickets(@Nullable User user, @NonNull Event event, @NonNull LocalDateTime dateTime, @NonNull Long seat);

    void bookTickets(@Nullable String userEmail, @NonNull Long eventId, @NonNull LocalDateTime dateTime, @NonNull Long seat);

    Ticket bookTicket(@Nullable User user, @NonNull Event event, @NonNull LocalDateTime dateTime, @NonNull Long seat);

    Ticket bookTicket(@Nullable String userEmail, @NonNull Long eventId, @NonNull LocalDateTime dateTime, @NonNull Long seat);

    Set<Ticket> getPurchasedTicketsForEvent(@NonNull Event event, @NonNull LocalDateTime dateTime);

    Set<Ticket> getPurchasedTicketsForUser(@NonNull User user);

    Set<Ticket> getPurchasedTicketsByUserForEvent(User user, Event event, LocalDateTime dateTime);

    Boolean purchaseValidate(Ticket ticket);

    Set<Ticket> getTicketsByUser(User user);

    Set<Long> getAvailableSeats(@NonNull Event event, @NonNull LocalDateTime dateTime);

    Boolean isAvailableSeats(@NonNull Event event, @NonNull LocalDateTime dateTime, Long seat);
}

package org.ivanina.course.spring37.cinema.service;

import org.ivanina.course.spring37.cinema.domain.Ticket;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.ivanina.course.spring37.cinema.domain.Event;
import org.ivanina.course.spring37.cinema.domain.User;

import java.time.LocalDateTime;
import java.util.Set;

public interface BookingService {
    double getTicketsPrice(@NonNull Event event,
                                  @NonNull LocalDateTime dateTime,
                                  @Nullable User user,
                                  @NonNull Set<Long> seats);

    void bookTickets(@NonNull Set<Ticket> tickets);

    void bookTickets(@Nullable User user, @NonNull Event event, @NonNull LocalDateTime dateTime, @NonNull Long seat);

    @NonNull Set<Ticket> getPurchasedTicketsForEvent(@NonNull Event event, @NonNull LocalDateTime dateTime);

    Boolean purchaseValidate(Ticket ticket);
}

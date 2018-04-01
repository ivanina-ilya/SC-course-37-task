package ua.epam.course.spring37.cinema.service;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ua.epam.course.spring37.cinema.dao.Dao;
import ua.epam.course.spring37.cinema.domain.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public interface EventService extends Dao<Event> {
    @Nullable
    Event getByName(@NonNull String name);

    Set<Event> getForDateRange(LocalDateTime from, LocalDateTime to);

    Set<Event> getNextEvents(LocalDateTime to);

    Set<Event> getAvailableEvents();

    Set<LocalDateTime> getAvailableEventDate(Event event);


}

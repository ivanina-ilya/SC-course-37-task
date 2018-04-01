package ua.epam.course.spring37.cinema.service;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ua.epam.course.spring37.cinema.dao.Dao;
import ua.epam.course.spring37.cinema.domain.Auditorium;

public interface AuditoriumService extends Dao<Auditorium> {
    public @Nullable Auditorium getByName(@NonNull String name);
}

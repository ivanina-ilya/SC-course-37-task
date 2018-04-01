package ua.epam.course.spring37.cinema.service;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ua.epam.course.spring37.cinema.dao.Dao;
import ua.epam.course.spring37.cinema.domain.User;

public interface UserService extends Dao<User> {
    @Nullable
    User getUserByEmail(@NonNull String email);
}

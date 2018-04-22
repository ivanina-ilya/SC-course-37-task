package org.ivanina.course.spring37.cinema.service;

import org.ivanina.course.spring37.cinema.dao.Dao;
import org.ivanina.course.spring37.cinema.domain.User;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface UserService extends Dao<User> {
    @Nullable
    User getUserByEmail(@NonNull String email);
}

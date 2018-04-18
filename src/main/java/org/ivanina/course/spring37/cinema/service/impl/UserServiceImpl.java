package org.ivanina.course.spring37.cinema.service.impl;

import org.apache.log4j.Logger;
import org.ivanina.course.spring37.cinema.dao.DomainStore;
import org.ivanina.course.spring37.cinema.dao.UserDao;
import org.ivanina.course.spring37.cinema.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.lang.Nullable;
import org.ivanina.course.spring37.cinema.domain.User;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;


public class UserServiceImpl implements UserService {

    private Logger log = Logger.getLogger(getClass());

    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;


    @Nullable
    @Override
    public User getUserByEmail(String email) {
        return userDao.getByEmail(email);
    }

    @Override
    public Long save(User user) {
        return userDao.save(user);
    }

    @Override
    public Set<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public User get(Long id) {
        return userDao.get(id);
    }

    @Override
    public Boolean remove(User entity) {
        return userDao.remove(entity);
    }

    @Override
    public Boolean remove(Long id) {
        return userDao.remove(id);
    }

    @Override
    public Long getNextIncrement() {
        return null;
    }
}

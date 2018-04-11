package org.ivanina.course.spring37.cinema.service.impl;

import org.ivanina.course.spring37.cinema.dao.DomainStore;
import org.ivanina.course.spring37.cinema.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.ivanina.course.spring37.cinema.domain.User;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Properties;


public class UserServiceImpl extends DomainStore<User> implements UserService {

    @Value("#{defaultUsers}")
    private Properties properties;

    @PostConstruct
    private void init() {
        Arrays.asList( properties.getProperty("elements.list").split(",") )
                .forEach( marker -> {
                    marker = marker.trim();
                    User user = new User(
                            properties.getProperty(marker+".firstName"),
                            properties.getProperty(marker+".lastName"),
                            properties.getProperty(marker+".email")
                    );
                    save(user);
                } );
    }

    @Nullable
    @Override
    public User getUserByEmail(String email) {
        return getAll().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst().orElse(null);
    }

    @Override
    public Long save(User user) {
        User validate = getUserByEmail(user.getEmail());
        if(validate != null && !validate.getId().equals(user.getId()))
            throw new IllegalArgumentException("Duplicate User by Email ["+user.getEmail()+"]");
        return super.save(user);
    }

}

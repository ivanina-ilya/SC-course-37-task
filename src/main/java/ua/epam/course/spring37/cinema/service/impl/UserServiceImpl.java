package ua.epam.course.spring37.cinema.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import ua.epam.course.spring37.cinema.dao.DomainStore;
import ua.epam.course.spring37.cinema.domain.Event;
import ua.epam.course.spring37.cinema.domain.EventRating;
import ua.epam.course.spring37.cinema.domain.User;
import ua.epam.course.spring37.cinema.service.UserService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Properties;


public class UserServiceImpl extends DomainStore<User> implements UserService {

    private Properties properties;

    @Nullable
    @Override
    public User getUserByEmail(String email) {
        return getAll().stream().filter(user -> user.getEmail().equalsIgnoreCase(email)).findFirst().get();
    }

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

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}

package ua.epam.course.spring37.cinema.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import ua.epam.course.spring37.cinema.dao.DomainStore;
import ua.epam.course.spring37.cinema.domain.Auditorium;
import ua.epam.course.spring37.cinema.service.AuditoriumService;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class AuditoriumServiceImpl extends DomainStore<Auditorium> implements AuditoriumService {

    private Properties properties;

    @Nullable
    @Override
    public Auditorium getByName(String name) {
        return getAll().stream().filter(auditorium -> auditorium.getName().equals(name)).findFirst().get();
    }

    private void init() {
        Arrays.asList(properties.getProperty("elements.list").split(","))
                .forEach(marker -> {
                    marker = marker.trim();
                    Auditorium auditorium = new Auditorium(properties.getProperty(marker + ".name"));
                    auditorium.setNumberOfSeats(Long.parseLong(properties.getProperty(marker + ".numberOfSeats")));
                    List<Long> vipSeats = Arrays.asList(properties.getProperty(marker + ".vipSeats").split(","))
                            .stream()
                            .map(item -> item.trim())
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
                    auditorium.setVipSeats(new HashSet<>(vipSeats));
                    save(auditorium);
                });
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}

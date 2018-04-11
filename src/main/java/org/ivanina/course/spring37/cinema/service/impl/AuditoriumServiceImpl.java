package org.ivanina.course.spring37.cinema.service.impl;

import org.ivanina.course.spring37.cinema.dao.DomainStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.ivanina.course.spring37.cinema.domain.Auditorium;
import org.ivanina.course.spring37.cinema.service.AuditoriumService;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class AuditoriumServiceImpl extends DomainStore<Auditorium> implements AuditoriumService {

    @Value("#{auditoriumProps}")
    private Properties properties;

    @Nullable
    @Override
    public Auditorium getByName(String name) {
        return getAll().stream().filter(auditorium -> auditorium.getName().equals(name)).findFirst().orElse(null);
    }

    @PostConstruct
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
}

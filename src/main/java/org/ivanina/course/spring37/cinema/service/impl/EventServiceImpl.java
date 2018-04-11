package org.ivanina.course.spring37.cinema.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.ivanina.course.spring37.cinema.dao.DomainStore;
import org.ivanina.course.spring37.cinema.domain.Event;
import org.ivanina.course.spring37.cinema.domain.EventRating;
import org.ivanina.course.spring37.cinema.service.AuditoriumService;
import org.ivanina.course.spring37.cinema.service.EventService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class EventServiceImpl extends DomainStore<Event> implements EventService {

    @Value("#{defaultEvensProps}")
    private Properties properties;

    @Value("#{auditoriumService}")
    private AuditoriumService auditoriumService;

    @PostConstruct
    private void init() {
        Arrays.asList( properties.getProperty("elements.list").split(",") )
                .forEach( marker -> {
                    marker = marker.trim();
                    Event event = new Event(properties.getProperty(marker+".name"));
                    event.setBasePrice( Double.parseDouble( properties.getProperty(marker+".basePrice")) );
                    event.setRating(EventRating.valueOf( properties.getProperty(marker+".rating")) );

                    Arrays.asList(properties.getProperty(marker+".airDates").split(","))
                            .forEach( air -> {
                                String[] item = air.trim().split("\\|");
                                event.addAirDateTime(
                                        LocalDateTime.parse(item[1]),
                                        auditoriumService.getByName(item[0])
                                );
                            } );

                    save(event);
                } );
    }

    @Nullable
    @Override
    public Event getByName(String name) {
        return getAll().stream().filter(user -> user.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public Set<Event> getForDateRange(LocalDateTime from, LocalDateTime to) {
        return getAll().stream()
                .filter( event -> event.getAirDates().stream()
                        .anyMatch( air -> air.isAfter(from) || air.isEqual(from) ) )
                .filter( event -> event.getAirDates().stream()
                        .anyMatch( air -> air.isBefore(to) || air.isEqual(to) ) )
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Event> getNextEvents(LocalDateTime to) {
        return getForDateRange( LocalDateTime.now(), to );
    }

    @Override
    public Set<Event> getAvailableEvents() {
        LocalDateTime now = LocalDateTime.now();
        return getAll().stream()
                .filter( event -> event.getAirDates().stream()
                        .anyMatch( air -> air.isAfter(now)  ) )
                .collect(Collectors.toSet());
    }

    @Override
    public Set<LocalDateTime> getAvailableEventDate(Event event) {
        LocalDateTime now = LocalDateTime.now();
        return event.getAirDates().stream()
                .filter( air -> air.isAfter(now))
                .collect(Collectors.toSet());
    }

    @Override
    public Event getNewEvent(String name) {
        return new Event(name);
    }
}

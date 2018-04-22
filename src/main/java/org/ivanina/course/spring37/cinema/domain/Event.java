package org.ivanina.course.spring37.cinema.domain;

import org.ivanina.course.spring37.cinema.service.Util;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;


public class Event extends DomainObject {

    @NonNull
    private String name;

    private NavigableSet<LocalDateTime> airDates = new TreeSet<>();

    private BigDecimal basePrice;

    private Long durationMilliseconds;

    private EventRating rating;

    private NavigableMap<LocalDateTime, Auditorium> auditoriums = new TreeMap<>();

    public Event(String name) {
        this.name = name;
    }

    public Event(Long id, String name, BigDecimal basePrice, Long durationMilliseconds, EventRating rating) {
        this.setId(id);
        this.name = name;
        this.basePrice = basePrice;
        this.durationMilliseconds = durationMilliseconds;
        this.rating = rating;
    }

    public boolean assignAuditorium(LocalDateTime dateTime, Auditorium auditorium) {
        if (airDates.contains(dateTime)) {
            auditoriums.put(dateTime, auditorium);
            return true;
        }
        return false;
    }

    public boolean removeAuditoriumAssignment(LocalDateTime dateTime) {
        return auditoriums.remove(dateTime) != null;
    }

    public boolean addAirDateTime(LocalDateTime dateTime) {
        return airDates.add(dateTime);
    }

    public boolean addAirDateTime(LocalDateTime dateTime, Auditorium auditorium) {
        boolean result = airDates.add(dateTime);
        if (result) {
            auditoriums.put(dateTime, auditorium);
        }
        return result;
    }

    public boolean removeAirDateTime(LocalDateTime dateTime) {
        boolean result = airDates.remove(dateTime);
        if (result) {
            auditoriums.remove(dateTime);
        }
        return result;
    }

    public boolean airsOnDateTime(LocalDateTime dateTime) {
        return airDates.stream().anyMatch(
                dt -> dateTime.isAfter(dt) && dateTime.isBefore(dt.plusSeconds(durationMilliseconds / 1000)));
    }

    public boolean airsOnDate(LocalDate date) {
        return airDates.stream().anyMatch(dt -> dt.toLocalDate().equals(date));
    }

    public boolean airsOnDates(LocalDate from, LocalDate to) {
        return airDates.stream()
                .anyMatch(dt -> dt.toLocalDate().compareTo(from) >= 0 && dt.toLocalDate().compareTo(to) <= 0);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NavigableSet<LocalDateTime> getAirDates() {
        // THis is a logic mistake or I forgot what I wanted to do:
        //return auditoriums != null ?  new TreeSet<>(auditoriums.keySet()) : null ;
        return airDates;
    }

    public void setAirDates(NavigableSet<LocalDateTime> airDates) {
        this.airDates = airDates;
    }


    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public EventRating getRating() {
        return rating;
    }

    public void setRating(EventRating rating) {
        this.rating = rating;
    }

    public NavigableMap<LocalDateTime, Auditorium> getAuditoriums() {
        return auditoriums;
    }

    public void setAuditoriums(NavigableMap<LocalDateTime, Auditorium> auditoriums) {
        this.auditoriums = auditoriums;
        if (this.airDates == null) this.airDates = new TreeSet<>();
        this.airDates.addAll(auditoriums.keySet());
    }

    public Long getDurationMilliseconds() {
        return durationMilliseconds;
    }

    public void setDurationMilliseconds(Long durationMilliseconds) {
        this.durationMilliseconds = durationMilliseconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return name.equals(event.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Event '%s' with %s rating; Price: %s (ID: %d)\n    Schedule:\n%s",
                name,
                rating.name(),
                NumberFormat.getCurrencyInstance().format(basePrice),
                getId(),
                getAuditoriums().entrySet().stream()
                        .map(entry -> String.format("%10s%s. Auditorium: %s",
                                "",
                                Util.localDateTimeFormatterDayTime(entry.getKey()),
                                entry.getValue().getName()))
                        .collect(Collectors.joining("\n")
                        ));
    }

    public String toStringWithoutSchedule() {
        return String.format("Event '%s' with %s rating; Price: %s (ID: %d)",
                name,
                rating.name(),
                NumberFormat.getCurrencyInstance().format(basePrice),
                getId());
    }
}

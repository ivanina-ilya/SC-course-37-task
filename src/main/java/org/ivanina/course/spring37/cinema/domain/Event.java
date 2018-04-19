package org.ivanina.course.spring37.cinema.domain;

import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;


public class Event extends DomainObject {

    @NonNull
    private String name;

    private NavigableSet<LocalDateTime> airDates = new TreeSet<>();

    private Double basePrice;

    private Long durationMilliseconds;

    private EventRating rating;

    private NavigableMap<LocalDateTime, Auditorium> auditoriums = new TreeMap<>();

    public Event(String name) {
        this.name = name;
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
                dt -> dateTime.isAfter(dt) && dateTime.isBefore(dt.plusSeconds( durationMilliseconds/1000 ))  );
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
        return airDates;
    }

    public void setAirDates(NavigableSet<LocalDateTime> airDates) {
        this.airDates = airDates;
    }


    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
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
        if(this.airDates == null) this.airDates = new TreeSet<>();
        this.airDates.addAll( auditoriums.keySet() );
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
        return String.format("Event '%s' with %s rating; Price: %d (ID: %d)",
                name, rating.name(), basePrice, getId()) ;
    }
}

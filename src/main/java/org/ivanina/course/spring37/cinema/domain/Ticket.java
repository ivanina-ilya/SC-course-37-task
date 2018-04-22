package org.ivanina.course.spring37.cinema.domain;

import org.ivanina.course.spring37.cinema.service.Util;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Ticket extends DomainObject implements Comparable<Ticket> {

    private User user;

    @NonNull
    private Event event;

    @NonNull
    private LocalDateTime dateTime;


    private Long seat;

    private BigDecimal price;

    public Ticket(User user, Event event, LocalDateTime dateTime, Long seat) {
        this.user = user;
        this.event = event;
        this.dateTime = dateTime;
        this.seat = seat;
    }

    public Ticket(Long id, User user, Event event, LocalDateTime dateTime, Long seat, BigDecimal price) {
        this.setId(id);
        this.user = user;
        this.event = event;
        this.dateTime = dateTime;
        this.seat = seat;
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LocalDateTime getDateTime() {
        return dateTime.withNano(0);
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime.withNano(0);
    }

    public Long getSeat() {
        return seat;
    }

    public void setSeat(Long seat) {
        this.seat = seat;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (user != null ? !user.equals(ticket.user) : ticket.user != null) return false;
        if (!event.equals(ticket.event)) return false;
        if (!dateTime.equals(ticket.dateTime)) return false;
        return seat != null ? seat.equals(ticket.seat) : ticket.seat == null;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + event.hashCode();
        result = 31 * result + dateTime.hashCode();
        result = 31 * result + (seat != null ? seat.hashCode() : 0);
        return result;
    }


    @Override
    public int compareTo(Ticket o) {
        if (o == null) {
            return 1;
        }
        int result = dateTime.compareTo(o.getDateTime());

        if (result == 0) {
            result = event.getName().compareTo(o.getEvent().getName());
        }
        if (result == 0) {
            result = Long.compare(seat, o.getSeat());
        }
        return result;
    }

    @Override
    public String toString() {
        return "Ticket for data: " + Util.localDateTimeFormatterDay(dateTime) + ", at time: " +
                Util.localDateTimeFormatterTime(dateTime) + "\n" +
                user + "\n" +
                event.toStringWithoutSchedule() + "\n" +
                "Seat: " + seat;
    }
}

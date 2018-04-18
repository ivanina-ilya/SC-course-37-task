package org.ivanina.course.spring37.cinema.domain;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public class Ticket extends DomainObject implements Comparable<Ticket> {
    private User user;

    @NonNull
    private Event event;

    @NonNull
    private LocalDateTime dateTime;

    private Long seat;

    private Double price;

    public Ticket(User user, Event event, LocalDateTime dateTime, Long seat) {
        this.user = user;
        this.event = event;
        this.dateTime = dateTime;
        this.seat = seat;
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
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getSeat() {
        return seat;
    }

    public void setSeat(Long seat) {
        this.seat = seat;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
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
        return "Ticket:\n" +
                user + "\n"+
                event + "\n"+
                "dateTime: " + dateTime + "\n"+
                "Seat: " + seat ;
    }
}

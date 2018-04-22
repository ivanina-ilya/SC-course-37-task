package org.ivanina.course.spring37.cinema.service.impl;

import org.ivanina.course.spring37.cinema.dao.TicketDao;
import org.ivanina.course.spring37.cinema.domain.Auditorium;
import org.ivanina.course.spring37.cinema.domain.Event;
import org.ivanina.course.spring37.cinema.domain.Ticket;
import org.ivanina.course.spring37.cinema.domain.User;
import org.ivanina.course.spring37.cinema.service.BookingService;
import org.ivanina.course.spring37.cinema.service.DiscountService;
import org.ivanina.course.spring37.cinema.service.EventService;
import org.ivanina.course.spring37.cinema.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookingServiceImpl implements BookingService {

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("discountService")
    private DiscountService discountService;

    @Autowired
    @Qualifier("eventService")
    private EventService eventService;

    @Autowired
    @Qualifier("ticketDao")
    private TicketDao ticketDao;


    @Override
    public BigDecimal getTicketsPrice(Event event, LocalDateTime dateTime, @Nullable User user, Set<Long> seats) {
        BigDecimal basePrice = discountService.calculatePrice(
                        event.getBasePrice(),
                        discountService.getDiscount(user, event, dateTime, seats.size())
        );

        basePrice = basePrice.setScale(2, RoundingMode.HALF_UP);
        return basePrice;
    }


    @Override
    public Set<Ticket> getPurchasedTicketsForEvent(Event event, LocalDateTime dateTime) {
        return ticketDao.getTicketsByEvent(event.getId(), dateTime);
    }

    @Override
    public Set<Ticket> getPurchasedTicketsByUserForEvent(User user, Event event, LocalDateTime dateTime) {
        return ticketDao.getTicketsByUserForEvent(user.getId(), event.getId(), dateTime);
    }

    public Set<Ticket> getTicketsByUserOnHand(User user) {
        return user.getTickets();
    }

    public Set<Ticket> getTicketsByUserOnHand(String email) {
        return getTicketsByUserOnHand(userService.getUserByEmail(email));
    }

    @Override
    public Set<Ticket> getTicketsByUser(User user) {
        return ticketDao.getTicketsByUser(user.getId());
    }

    @Override
    public void bookTickets(Set<Ticket> tickets) {
        tickets.forEach(this::purchaseValidate);
        tickets.forEach(ticket -> {
            ticket.setPrice(
                    discountService.calculatePrice(
                            ticket.getEvent().getBasePrice(),
                            discountService.getDiscount(ticket.getUser(), ticket.getEvent(), ticket.getDateTime(), tickets.size())
                    )
            );
            ticketDao.save(ticket);
        });
    }

    @Override
    public void bookTickets(@Nullable User user, Event event, LocalDateTime dateTime, Long seat) {
        bookTickets(new HashSet<>(Collections.singletonList(new Ticket(user, event, dateTime, seat))));
    }

    @Override
    public void bookTickets(@Nullable String userEmail, Long eventId, LocalDateTime dateTime, Long seat) {
        User user = userService.getUserByEmail(userEmail);
        if (user == null)
            throw new IllegalArgumentException("The User with email '" + userEmail + "' does not exist");
        Event event = eventService.get(eventId);
        if (event == null)
            throw new IllegalArgumentException("The event not available [ID: " + event + "]");

        bookTickets(user, event, dateTime, seat);
    }


    @Override
    public Ticket bookTicket(@Nullable User user, Event event, LocalDateTime dateTime, Long seat) {
        Ticket ticket = new Ticket(user, event, dateTime, seat);
        ticket.setPrice(
                discountService.calculatePrice(event.getBasePrice(),
                        discountService.getDiscount(user, event, dateTime, 1L)));
        purchaseValidate(ticket);
        ticket.setId(ticketDao.save(ticket));
        return ticket;
    }

    @Override
    public Ticket bookTicket(@Nullable String userEmail, Long eventId, LocalDateTime dateTime, Long seat) {
        User user = userService.getUserByEmail(userEmail);
        if (user == null)
            throw new IllegalArgumentException("The User with email '" + userEmail + "' does not exist");
        Event event = eventService.get(eventId);
        if (event == null)
            throw new IllegalArgumentException("The event not available [ID: " + event + "]");
        return bookTicket(user, event, dateTime, seat);
    }

    @Override
    public Boolean purchaseValidate(Ticket ticket) {
        Event event = eventService.get(ticket.getEvent().getId());
        if (event == null)
            throw new IllegalArgumentException("The event not available [" + ticket.getEvent() + "]");
        if (eventService.getAvailableEventDate(event).stream()
                .noneMatch(date -> ticket.getDateTime().isEqual(date)))
            throw new IllegalArgumentException("The date [" + ticket.getDateTime() + "] for event not available [" + ticket.getEvent() + "]");
        if (event.getAuditoriums().get(ticket.getDateTime()) == null)
            throw new IllegalArgumentException("The date [" + ticket.getDateTime() + "] for event not available [" + ticket.getEvent() + "]");
        if (ticketDao.getTicketsByEvent(ticket.getEvent().getId(), ticket.getDateTime()).stream()
                .anyMatch(storeTicket -> storeTicket.getSeat().equals(ticket.getSeat())))
            throw new IllegalArgumentException("The seat [" + ticket.getSeat() + "] for event not available [" + ticket.getEvent() + "]");
        return true;
    }


    @Override
    public Set<Ticket> getPurchasedTicketsForUser(User user) {
        return ticketDao.getTicketsByUser(user.getId());
    }

    @Override
    public Set<Long> getAvailableSeats(Event event, LocalDateTime dateTime) {
        Auditorium auditorium = event.getAuditoriums() != null ?
                event.getAuditoriums().get(dateTime) :
                null;
        if (auditorium == null)
            throw new IllegalArgumentException("No Auditorium assigned");
        Set<Ticket> tickets = getPurchasedTicketsForEvent(event, dateTime);
        if (tickets == null || tickets.size() == 0) return auditorium.getSeats();

        List<Long> boughSeats = tickets.stream()
                .map(Ticket::getSeat)
                .collect(Collectors.toList());

        return auditorium.getSeats().stream()
                .filter(seat -> !boughSeats.contains(seat))
                .collect(Collectors.toSet());
    }

    @Override
    public Boolean isAvailableSeats(Event event, LocalDateTime dateTime, Long seat) {
        return getAvailableSeats(event, dateTime).contains(seat);
    }
}

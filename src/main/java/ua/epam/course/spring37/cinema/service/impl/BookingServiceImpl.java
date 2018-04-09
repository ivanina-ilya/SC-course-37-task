package ua.epam.course.spring37.cinema.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import ua.epam.course.spring37.cinema.dao.DomainStore;
import ua.epam.course.spring37.cinema.domain.Event;
import ua.epam.course.spring37.cinema.domain.Ticket;
import ua.epam.course.spring37.cinema.domain.User;
import ua.epam.course.spring37.cinema.service.BookingService;
import ua.epam.course.spring37.cinema.service.DiscountService;
import ua.epam.course.spring37.cinema.service.EventService;
import ua.epam.course.spring37.cinema.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BookingServiceImpl extends DomainStore<Ticket> implements BookingService {

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("discountService")
    private DiscountService discountService;

    @Autowired
    @Qualifier("eventsService")
    private EventService eventService;


    @Override
    public double getTicketsPrice(Event event, LocalDateTime dateTime, @Nullable User user, Set<Long> seats) {
        byte discount = discountService.getDiscount(user, event, dateTime, seats.size());
        Double basePrice = event.getBasePrice();
        basePrice *= event.getRating().getCoefficient();
        basePrice = basePrice - basePrice * discount / 100;
        return basePrice;
    }


    @Override
    public Set<Ticket> getPurchasedTicketsForEvent(Event event, LocalDateTime dateTime) {
        return null;
    }

    public Set<Ticket> getTicketsByUserOnHand(String email) {
        return getTicketsByUserOnHand( userService.getUserByEmail(email) );
    }

    public Set<Ticket> getTicketsByUserOnHand(User user) {
        return user.getTickets();
    }

    public Set<Ticket> getTicketsByUser(User user) {
        return getAll().stream()
                .filter(ticket -> ticket.getUser() != null && ticket.getUser().getEmail().equalsIgnoreCase( user.getEmail() ))
                .collect(Collectors.toSet());
    }

    @Override
    public void bookTickets(Set<Ticket> tickets) {
        tickets.forEach(this::purchaseValidate);
        tickets.forEach(this::save);
    }

    @Override
    public void bookTickets(@Nullable User user, Event event, LocalDateTime dateTime, Long seat) {
        bookTickets( new HashSet<>(Collections.singletonList(new Ticket(user, event, dateTime, seat))) );
    }

    @Override
    public Boolean purchaseValidate(Ticket ticket) {
        Event event = eventService.get( ticket.getEvent().getId());
        if(event == null)
            throw new IllegalArgumentException("The event not available ["+ticket.getEvent()+"]");
        if (eventService.getAvailableEventDate(event).stream()
                .noneMatch( date -> ticket.getDateTime().isEqual(date) ))
            throw new IllegalArgumentException("The date ["+ticket.getDateTime()+"] for event not available ["+ticket.getEvent()+"]");

        // same as previous by logic
        if (event.getAuditoriums().get(ticket.getDateTime()) == null )
            throw new IllegalArgumentException("The date ["+ticket.getDateTime()+"] for event not available ["+ticket.getEvent()+"]");

        if (getAll().stream()
                .filter( storeTicket -> storeTicket.getEvent().equals(event))
                .filter( storeTicket -> storeTicket.getDateTime().isEqual(ticket.getDateTime()))
                .anyMatch( storeTicket -> storeTicket.getSeat().equals(ticket.getSeat()) ) )
            throw new IllegalArgumentException("The seat ["+ticket.getSeat()+"] for event not available ["+ticket.getEvent()+"]");

        return true;
    }






    /*public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }*/


}

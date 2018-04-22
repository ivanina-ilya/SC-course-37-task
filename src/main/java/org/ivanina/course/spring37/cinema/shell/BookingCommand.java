package org.ivanina.course.spring37.cinema.shell;

import org.ivanina.course.spring37.cinema.domain.Event;
import org.ivanina.course.spring37.cinema.domain.Ticket;
import org.ivanina.course.spring37.cinema.domain.User;
import org.ivanina.course.spring37.cinema.service.*;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.shell.standard.ShellComponent;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@ShellComponent
public class BookingCommand implements CommandMarker {
    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "eventService")
    private EventService eventService;

    @Resource(name = "auditoriumService")
    private AuditoriumService auditoriumService;

    @Resource(name = "bookingService")
    private BookingService bookingService;

    @Resource(name = "discountService")
    private DiscountService discountService;

    @CliAvailabilityIndicator({
            "buyTicket",
            "getPurchasedTicketsForEvent"
    })
    public boolean isCommandAvailable() {
        return true;
    }

    @CliCommand(
            value = "buyTicket",
            help = "Prepare and validate the ticket without buying. \n" +
                    "The seats can be separated by comma. \n" +
                    "Date and time in format: yyyy-MM-dd HH:mm:ss")
    public String buyTicket(
            @CliOption(key = {"event_id"}, mandatory = true, help = "Event ID") final Long eventId,
            @CliOption(key = {"date_time"}, mandatory = true, help = "Date and time in format: yyyy-MM-dd HH:mm:ss") final String dateTime,
            @CliOption(key = {"seats"}, mandatory = true, help = "The seats can be separated by comma.") final String seats,
            @CliOption(key = {"email"}, mandatory = true, help = "The user's E-Mail.") final String email) {
        try {
            LocalDateTime localDateTime = Util.localDateTimeParse(dateTime);
            Event event = eventService.get(eventId);
            if (event == null) return "Does not exist Event with ID " + eventId;
            Set<Long> seatsSet = seatsParse(seats);

            User user = userService.getUserByEmail(email);
            Set<Ticket> tickets = getTickets(event, localDateTime, seatsSet, user);
            bookingService.bookTickets(tickets);
            return "Requested seats: '" +
                    seatsSet.stream().map(Object::toString).collect(Collectors.joining(", "))
                    + "' for event: " + event +
                    "\nTo auditorium: " + event.getAuditoriums().get(localDateTime) +
                    "\nTickets count: " + tickets.size() + "\n" +
                    "\n======================\n" +
                    tickets.stream()
                            .map(Ticket::toString)
                            .collect(Collectors.joining("\n-------\n\n"));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @CliCommand(value = "getPurchasedTicketsForEvent", help = "Get all User's Tickets for the Event")
    public String getPurchasedTicketsForEvent(
            @CliOption(key = {"event_id"}, mandatory = true, help = "Event ID") final Long eventId,
            @CliOption(key = {"date_time"}, mandatory = true, help = "Date and time in format: yyyy-MM-dd HH:mm:ss") final String dateTime,
            @CliOption(key = {"email"}, mandatory = true, help = "The user's E-Mail.") final String email
    ) {
        Event event = eventService.get(eventId);
        if (event == null) return "Does not exist Event with ID " + eventId;
        User user = userService.getUserByEmail(email);
        if (event == null) return "Does not exist User with email " + email;

        return bookingService.getPurchasedTicketsByUserForEvent(user, event, Util.localDateTimeParse(dateTime)).stream()
                .map(Ticket::toString)
                .collect(Collectors.joining("\n-------\n\n"));
    }

    @CliCommand(value = "getAvailableSeats", help = "Get available seats for the Event")
    public String getAvailableSeats(
            @CliOption(key = {"event_id"}, mandatory = true, help = "Event ID") final Long eventId,
            @CliOption(key = {"date_time"}, mandatory = true, help = "Date and time in format: yyyy-MM-dd HH:mm:ss") final String dateTime
    ) {
        Event event = eventService.get(eventId);
        if (event == null) return "Does not exist Event with ID " + eventId;
        return bookingService.getAvailableSeats(event, Util.localDateTimeParse(dateTime)).stream()
                .map(Object::toString).collect(Collectors.joining(", "));
    }


    private Set<Ticket> getTickets(Event event, LocalDateTime dateTime, Set<Long> seats, User user) {
        return seats.stream()
                .map(seat -> new Ticket(null, user, event, dateTime, seat,
                        bookingService.getTicketsPrice(event, dateTime, user, seats)))
                .collect(Collectors.toSet());

    }

    private Set<Long> seatsParse(String seats) {
        return Arrays.stream(seats.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

}

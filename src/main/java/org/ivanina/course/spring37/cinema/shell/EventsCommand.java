package org.ivanina.course.spring37.cinema.shell;

import org.ivanina.course.spring37.cinema.domain.Auditorium;
import org.ivanina.course.spring37.cinema.domain.Event;
import org.ivanina.course.spring37.cinema.domain.EventRating;
import org.ivanina.course.spring37.cinema.service.*;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.shell.standard.ShellComponent;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@ShellComponent
public class EventsCommand implements CommandMarker {

    @Resource(name = "eventService")
    private EventService eventService;

    @Resource(name = "auditoriumService")
    private AuditoriumService auditoriumService;


    @CliAvailabilityIndicator({
            "getEvents",
            "getEventById",
            "addEvent",
            "assignAuditorium",
            "getAuditoriums",
            "getAuditorium",
            "getVipSeats",
            "getVipSeatsInAuditorium"
    })
    public boolean isCommandAvailable() {
        return true;
    }

    @CliCommand(value = "getEvents", help = "List of all events")
    public String getEvents() {
        return Util.shellOutputFormat(eventService.getAllFull().stream()
                .map(Event::toString)
                .collect(Collectors.joining("\n-------\n")));
    }

    @CliCommand(value = "getEventById", help = "Get event by ID")
    public String getEventById(
            @CliOption(key = {"id"}, mandatory = true, help = "ID") final Long id
    ) {
        return Util.shellOutputFormat(eventService.get(id).toString());
    }

    @CliCommand(value = "addEvent", help = "Add event without assign to auditorium")
    public String addEvent(
            @CliOption(key = {"name"}, mandatory = true, help = "Title of event") final String name,
            @CliOption(key = {"price"}, mandatory = true, help = "The Base Price") final BigDecimal basePrice,
            @CliOption(key = {"duration"}, mandatory = true, help = "The the Duration in minutes") final Long duration,
            @CliOption(key = {"rating"}, mandatory = true, help = "The Rating. Possible values: LOW | MID | HIGH") final String rating
    ) {
        try {
            Event event = new Event(null, name, basePrice, duration * 60 * 1000, EventRating.valueOf(rating));
            eventService.save(event);
            return Util.shellOutputFormat(event.toString());
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @CliCommand(value = "assignAuditorium", help = "Assign Auditorium to Event")
    public String assignAuditorium(
            @CliOption(key = {"event_id"}, mandatory = true, help = "Event ID") final Long eventId,
            @CliOption(key = {"auditorium_id"}, mandatory = true, help = "Auditorium ID") final Long auditoriumId,
            @CliOption(key = {"date_time"}, mandatory = true, help = "Date and time in format: yyyy-MM-dd HH:mm:ss") final String dateTime
    ) {
        try {
            Event event = eventService.get(eventId);
            Auditorium auditorium = auditoriumService.get(auditoriumId);
            event.addAirDateTime(Util.localDateTimeParse(dateTime), auditorium);
            eventService.save(event);
            return Util.shellOutputFormat(event.toString());
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    @CliCommand(value = "getAuditoriums", help = "List of all Auditoriums")
    public String getAuditoriums() {
        return Util.shellOutputFormat(auditoriumService.getAll().stream()
                .map(Auditorium::toString)
                .collect(Collectors.joining("\n-------\n")));
    }

    @CliCommand(value = "getAuditorium", help = "Get Auditorium by ID")
    public String getAuditorium(
            @CliOption(key = {"auditorium_id"}, mandatory = true, help = "Auditorium ID") final Long auditoriumId
    ) {
        return Util.shellOutputFormat(auditoriumService.get(auditoriumId).toString());
    }


    @CliCommand(value = "getVipSeats", help = "Get VIP seats for the Event")
    public String getVipSeats(
            @CliOption(key = {"event_id"}, mandatory = true, help = "Event ID") final Long eventId,
            @CliOption(key = {"date_time"}, mandatory = true, help = "Date and time in format: yyyy-MM-dd HH:mm:ss") final String dateTime
    ) {
        try {
            Event event = eventService.get(eventId);
            if (event == null) return "Does not exist Event with ID " + eventId;
            Auditorium auditorium = event.getAuditoriums().get(Util.localDateTimeParse(dateTime));
            return getVipSeats(auditorium).stream()
                    .map(Object::toString).collect(Collectors.joining(", "));
        } catch (Exception e){
            return e.getMessage();
        }

    }

    @CliCommand(value = "getVipSeatsInAuditorium", help = "Get VIP seats in Auditorium")
    public String getVipSeatsInAuditorium(
            @CliOption(key = {"auditorium_id"}, mandatory = true, help = "Auditorium ID") final Long auditoriumId
    ) {
        try {
            return getVipSeats(auditoriumId).stream()
                    .map(Object::toString).collect(Collectors.joining(", "));
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    private Set<Long> getVipSeats(Long auditoriumId){
        Auditorium auditorium = auditoriumService.get(auditoriumId);
        if (auditorium == null) throw new IllegalArgumentException("Does not exist Auditorium with ID " + auditoriumId);
        return getVipSeats(auditorium);
    }

    private Set<Long> getVipSeats(Auditorium auditorium){
        if (auditorium == null) throw new IllegalArgumentException("Does not exist Auditorium ");
        if (auditorium.getVipSeats() == null) throw new IllegalArgumentException("No VIP seats in Auditorium:" + auditorium);
        return auditorium.getVipSeats();
    }
}

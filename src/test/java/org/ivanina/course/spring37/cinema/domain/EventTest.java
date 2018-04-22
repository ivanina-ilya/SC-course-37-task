package org.ivanina.course.spring37.cinema.domain;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

public class EventTest {

    private Event event;
    private String name = "Test Gold Event";
    private Auditorium auditorium;

    @Before
    public void init() {
        auditorium = new Auditorium("Golden Test");
        auditorium.setNumberOfSeats(100);
        auditorium.setVipSeats( new HashSet<>( Arrays.asList(50L,51L,52L,53L,54L,55L)) );

        event = new Event(name);
        event.setBasePrice( new BigDecimal( 99.99));
        event.setRating( EventRating.HIGH );
        event.addAirDateTime(LocalDateTime.parse("2018-01-01T10:00:00"),auditorium);
        event.addAirDateTime(LocalDateTime.parse("2018-01-01T12:00:00"),auditorium);
    }

    @Test
    public void addAirDateTimeAndDuplicateTest(){
        int cntAirEvent = event.getAirDates().size();
        int cntAssignAuditoriums = event.getAuditoriums().size();
        boolean result = event.addAirDateTime(LocalDateTime.parse("2018-01-01T10:00:00"),auditorium);

        assertFalse(result);
        assertEquals(cntAirEvent, event.getAirDates().size());
        assertEquals(cntAssignAuditoriums, event.getAuditoriums().size());

        result = event.addAirDateTime(LocalDateTime.parse("2018-11-11T11:00:00"),auditorium);
        assertTrue(result);
        assertEquals(cntAirEvent+1, event.getAirDates().size());
        assertEquals(cntAssignAuditoriums+1, event.getAuditoriums().size());
    }

    @Test
    public void assignAuditoriumTest() {
        int cntAuditoriums = event.getAuditoriums().size();
        LocalDateTime dateTime = LocalDateTime.parse("2018-12-31T10:00:00");
        event.addAirDateTime(dateTime);
        boolean result;

        result = event.assignAuditorium( dateTime.plusDays(5), auditorium );
        assertFalse(result);
        result = event.assignAuditorium( dateTime, auditorium );
        assertTrue(result);
        assertEquals(cntAuditoriums+1 , event.getAuditoriums().size());

        event.removeAuditoriumAssignment(dateTime);
        assertEquals(cntAuditoriums , event.getAuditoriums().size());
    }
}

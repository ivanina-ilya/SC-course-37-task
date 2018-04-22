package org.ivanina.course.spring37.cinema.service;

import org.ivanina.course.spring37.cinema.config.ApplicationSpringConfig;
import org.ivanina.course.spring37.cinema.config.JdbcConfig;
import org.ivanina.course.spring37.cinema.dao.TicketDao;
import org.ivanina.course.spring37.cinema.domain.Auditorium;
import org.ivanina.course.spring37.cinema.domain.Event;
import org.ivanina.course.spring37.cinema.domain.Ticket;
import org.ivanina.course.spring37.cinema.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationSpringConfig.class, JdbcConfig.class})
public class BookingServiceTest {
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
    @Qualifier("bookingService")
    private BookingService bookingService;

    @Autowired
    @Qualifier("ticketDao")
    private TicketDao ticketDao;

    User user;
    Event event;
    LocalDateTime dateTime ;
    Auditorium auditorium ;

    @Before
    public void init(){
        user = userService.get(1L);
        event = eventService.get(1L);

        dateTime = event.getAuditoriums().firstEntry().getKey();
        auditorium = event.getAuditoriums().firstEntry().getValue();
    }

    @Test
    public void getTicketsPriceTest(){
        Set<Long> seats = new HashSet<>(Arrays.asList(15L,16L,17L) );
        assertEquals(event.getBasePrice(), bookingService.getTicketsPrice( event, dateTime, user, seats ));
    }

    @Test
    public void getTicketsPriceDiscountTest(){
        Set<Long> seats = new HashSet<>(Arrays.asList(15L,16L,17L,1L,2L,3L,4L,5L,6L,7L,8L) );
        BigDecimal difference =  event.getBasePrice().multiply( new BigDecimal(50) ).divide(new BigDecimal(100));
        assertEquals(
                event.getBasePrice().subtract(difference).setScale(2, RoundingMode.HALF_UP),
                bookingService.getTicketsPrice( event, dateTime, user, seats )
        );
    }

    @Test
    public void getPurchasedTicketTest(){
        Ticket ticket = new Ticket(null, user, event, dateTime, 15L, new BigDecimal(55.55));
        Ticket ticketBought = bookingService.bookTicket(user, event, dateTime, 1L );

        assertNotNull(ticketBought.getId());
        ticketDao.remove(ticketBought);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPurchasedTicketDuplicateTest(){
        Ticket ticketBought = bookingService.bookTicket(user, event, dateTime, 1L );
        assertNotNull(ticketBought.getId());

        bookingService.bookTicket(user, event, dateTime, 1L );
    }

    @Test
    public void getAvailableSeatsTest() {
        Set<Long> availableSeats =  bookingService.getAvailableSeats(event, dateTime);
        bookingService.bookTicket(user, event, dateTime, 1L );

        assertEquals(
                availableSeats.size()-1,
                bookingService.getAvailableSeats(event, dateTime).size());
    }

    @Test
    public void isAvailableSeatsTest() {
        assertTrue( bookingService.isAvailableSeats(event, dateTime, 7L) );
        bookingService.bookTicket(user, event, dateTime, 7L );
        assertFalse(bookingService.isAvailableSeats(event, dateTime, 7L));
    }



}

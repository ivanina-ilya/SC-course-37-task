package org.ivanina.course.spring37.cinema.dao;

import org.ivanina.course.spring37.cinema.config.ApplicationSpringConfig;
import org.ivanina.course.spring37.cinema.config.JdbcConfig;
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
import java.util.NavigableSet;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationSpringConfig.class, JdbcConfig.class})
public class TicketDaoTest {

    @Autowired
    @Qualifier("ticketDao")
    private TicketDao ticketDao;

    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;

    @Autowired
    @Qualifier("eventDao")
    private EventDao eventDao;

    User user;
    Event event;
    LocalDateTime dateTime ;
    Auditorium auditorium ;

    @Before
    public void init(){
        user = userDao.get(1L);
        event = eventDao.get(1L);

        dateTime = event.getAuditoriums().firstEntry().getKey();
        auditorium = event.getAuditoriums().firstEntry().getValue();
    }

    @Test
    public void addTicketTest(){
        int cnt = ticketDao.getAll().size();

        Ticket ticket = new Ticket(null, user, event, dateTime, 5L,
                new BigDecimal(55.55).setScale(2, RoundingMode.HALF_UP));
        Long id = ticketDao.save(ticket);

        assertNotNull(id);
        assertNotEquals(new Long(0), id);

        assertEquals(cnt+1, ticketDao.getAll().size());

        ticketDao.remove(ticket);

        assertEquals(cnt, ticketDao.getAll().size());
        assertNull(ticket.getId());
    }

    @Test
    public void getTicketsByUser(){
        NavigableSet<Ticket> tickets = ticketDao.getTicketsByUser(user.getId());
        int cnt = tickets == null ? 0 : tickets.size();

        Ticket ticket = new Ticket(null, user, event, dateTime, 15L, new BigDecimal(100.00));
        ticketDao.save(ticket);

        assertEquals(cnt+1, ticketDao.getTicketsByUser(user.getId()).size());
    }

    @Test
    public void getTicketsByEvent(){
        NavigableSet<Ticket> tickets = ticketDao.getTicketsByEvent(event.getId(), dateTime);
        int cnt = tickets == null ? 0 : tickets.size();

        Ticket ticket = new Ticket(null, user, event, dateTime, 10L, new BigDecimal(100.00));
        ticketDao.save(ticket);

        assertEquals(cnt+1, ticketDao.getTicketsByEvent(event.getId(), dateTime).size() );
    }
}

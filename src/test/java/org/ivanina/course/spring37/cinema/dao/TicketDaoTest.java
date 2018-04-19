package org.ivanina.course.spring37.cinema.dao;

import org.ivanina.course.spring37.cinema.config.ApplicationSpringConfig;
import org.ivanina.course.spring37.cinema.config.JdbcConfig;
import org.ivanina.course.spring37.cinema.domain.Auditorium;
import org.ivanina.course.spring37.cinema.domain.Event;
import org.ivanina.course.spring37.cinema.domain.Ticket;
import org.ivanina.course.spring37.cinema.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

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

    @Test
    public void addTicketTest(){
        User user = userDao.get(1L);
        Event event = eventDao.get(1L);
        LocalDateTime dateTime = event.getAuditoriums().firstEntry().getKey();
        Auditorium auditorium = event.getAuditoriums().firstEntry().getValue();

        int cnt = ticketDao.getAll().size();

        Ticket ticket = new Ticket(null, user, event, dateTime, 15L, 55.55);
        Long id = ticketDao.save(ticket);

        assertNotNull(id);
        assertNotEquals(new Long(0), id);

        assertEquals(cnt+1, ticketDao.getAll().size());

        ticketDao.remove(ticket);

        assertEquals(cnt, ticketDao.getAll().size());
        assertNull(ticket.getId());
    }
}

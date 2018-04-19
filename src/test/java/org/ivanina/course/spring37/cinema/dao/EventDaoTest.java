package org.ivanina.course.spring37.cinema.dao;

import org.ivanina.course.spring37.cinema.config.ApplicationSpringConfig;
import org.ivanina.course.spring37.cinema.config.JdbcConfig;
import org.ivanina.course.spring37.cinema.domain.Auditorium;
import org.ivanina.course.spring37.cinema.domain.Event;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.NavigableMap;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationSpringConfig.class, JdbcConfig.class})
public class EventDaoTest {

    @Autowired
    @Qualifier("eventDao")
    private EventDao eventDao;

    @Autowired
    @Qualifier("auditoriumDao")
    private AuditoriumDao auditoriumDao;

    @Test
    public void getEventAuditoriumsTest(){
        NavigableMap<LocalDateTime, Auditorium> r = eventDao.getEventAuditoriums(1L);
        assertNotNull(r);
    }

    @Test
    public void addRemoveEventAuditoriumsTest(){
        LocalDateTime dateTime = LocalDateTime.of(2000, Month.APRIL, 1, 15, 0);
        Event event = eventDao.get(1L);
        assertNotNull(event);
        Auditorium auditorium = auditoriumDao.get(2L);
        int eventAuditoriums = event.getAuditoriums() != null ? event.getAuditoriums().size() : 0;

        assertTrue(event.addAirDateTime(
                dateTime,
                auditorium ));
        eventDao.save(event);

        Event event2check = eventDao.get(1L);
        assertNotNull(event2check);
        assertEquals(eventAuditoriums+1, event2check.getAuditoriums() != null ? event2check.getAuditoriums().size() : 0);

        assertTrue( event2check.removeAirDateTime(dateTime) );
        eventDao.save(event2check);

        Event event3check = eventDao.get(1L);
        assertNotNull(event3check);
        assertEquals(eventAuditoriums, event3check.getAuditoriums().size());
    }
}

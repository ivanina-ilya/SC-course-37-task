package org.ivanina.course.spring37.cinema.service;

import org.apache.log4j.Logger;
import org.ivanina.course.spring37.cinema.config.ApplicationSpringConfig;
import org.ivanina.course.spring37.cinema.aspects.CounterAspect;
import org.ivanina.course.spring37.cinema.config.JdbcConfig;
import org.ivanina.course.spring37.cinema.domain.Event;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationSpringConfig.class, JdbcConfig.class})
public class EventServiceTest {

    private Logger log = Logger.getLogger(EventServiceTest.class);

    @Autowired
    private EventService eventService;
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @Autowired
    private CounterAspect counterAspect;


    @After
    public void  afterEach(){
        log.info("counterAspect: \n"+counterAspect);
    }

    @Test
    public void getByNameTest(){
        Set<Event> eventList = eventService.getAll();
        Event event = eventList.stream().findFirst().get();

        Event eventByName = eventService.getByName( event.getName() );

        assertEquals(event.getName(),eventByName.getName());
        assertNotNull(eventByName.getId());
    }

    @Test
    public void addEventTest(){
        Event event = null;
        String name = "Test New Event";

        event = eventService.getByName(name);
        assertNull(event);

        event = new Event(name);
        Long id = eventService.save(event);

        event = eventService.getByName(name);
        assertNotNull(event);
        assertEquals(event.getName(),name);
        assertEquals(event.getId(),id);

    }


    @Test
    public void getSetBasePriceTest(){
        //Event event = eventService.getByName("Harry Potter");
        Event event = new Event("Harry Potter 2");

        //assertNull(event.getBasePrice());
        //assertEquals(event.getBasePrice(),25.5, 0.001);
        event.setBasePrice(100.00);
        assertEquals(event.getBasePrice(),100.00, 0.001);
    }


}

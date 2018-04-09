package ua.epam.course.spring37.cinema.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.epam.course.spring37.cinema.ApplicationSpringConfig;
import ua.epam.course.spring37.cinema.domain.Event;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationSpringConfig.class) // will be use test config
public class EventServiceTest {

    @Autowired
    private EventService eventService;
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @Before
    public void init() {
        // initialization in init method on service by default data from properties
        // SEE TO: defaultEvens.properties
    }

    @Test
    public void getByNameTest(){
        String name = "Harry Potter";
        Event event = eventService.getByName(name);

        assertEquals(event.getName(),name);
        assertNotNull(event.getId());
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




}

package ua.epam.course.spring37.cinema.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.epam.course.spring37.cinema.domain.Event;
import ua.epam.course.spring37.cinema.domain.EventRating;
import ua.epam.course.spring37.cinema.domain.User;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml") // will be use test spring.xml
public class DiscountServiceTest {

    private DiscountService discountService;
    @Autowired
    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }

    private AuditoriumService auditoriumService;
    @Autowired
    public void setAuditoriumService(AuditoriumService auditoriumService) {
        this.auditoriumService = auditoriumService;
    }

    @Test
    public void getDiscountTest(){
        LocalDateTime dateTime = LocalDateTime.parse("2018-04-04T15:00:00");
        User user = new User("John","test", "test@test.com");
        Event event = new Event("Test Event");
        event.setBasePrice(100.0);
        event.setRating(EventRating.HIGH);
        event.addAirDateTime(dateTime, auditoriumService.getByName("Gold"));

        byte discount = 0;
        discount = discountService.getDiscount(user,event,dateTime,5);
        assertEquals(0,discount);

        user.setBirthday( dateTime );
        discount = discountService.getDiscount(user,event,dateTime,5);
        assertEquals(5,discount);

        discount = discountService.getDiscount(user,event,dateTime,15);
        assertEquals(50,discount);
    }
}

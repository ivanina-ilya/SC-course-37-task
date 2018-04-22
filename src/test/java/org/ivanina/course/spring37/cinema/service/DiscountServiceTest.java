package org.ivanina.course.spring37.cinema.service;

import org.apache.log4j.Logger;
import org.ivanina.course.spring37.cinema.config.JdbcConfig;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.ivanina.course.spring37.cinema.config.ApplicationSpringConfig;
import org.ivanina.course.spring37.cinema.aspects.CounterAspect;
import org.ivanina.course.spring37.cinema.domain.Event;
import org.ivanina.course.spring37.cinema.domain.EventRating;
import org.ivanina.course.spring37.cinema.domain.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationSpringConfig.class, JdbcConfig.class})
public class DiscountServiceTest {

    private Logger log = Logger.getLogger(getClass());

    private DiscountService discountService;
    @Autowired
    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }

    @Autowired
    private CounterAspect counterAspect;

    @Autowired
    private AuditoriumService auditoriumService;


    @After
    public void  afterEach(){
        log.info("counterAspect: \n"+counterAspect);
    }

    @Test
    public void getDiscountTest(){
        LocalDateTime dateTime = LocalDateTime.parse("2018-04-04T15:00:00");
        User user = new User("John","test", "test@test.com");
        Event event = new Event("Test Event");
        event.setBasePrice( new BigDecimal(100.0 ));
        event.setRating(EventRating.HIGH);
        event.addAirDateTime(dateTime, auditoriumService.getByName("Gold"));

        byte discount = 0;
        discount = discountService.getDiscount(user,event,dateTime,5);
        assertEquals(0,discount);

        user.setBirthday( dateTime.toLocalDate() );
        discount = discountService.getDiscount(user,event,dateTime,5);
        assertEquals(5,discount);

        discount = discountService.getDiscount(user,event,dateTime,15);
        assertEquals(50,discount);
    }
}

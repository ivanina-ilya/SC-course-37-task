package ua.epam.course.spring37.cinema.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml") // will be use test spring.xml
public class BookingServiceTest {
    @Autowired
    private UserService userService;
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private DiscountService discountService;
    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }

    @Autowired
    private EventService eventService;
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @Test
    public void getTicketsPriceTest(){

    }

}

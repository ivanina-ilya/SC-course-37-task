package org.ivanina.course.spring37.cinema.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.ivanina.course.spring37.cinema.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class BookingAspect {

    @Autowired
    @Qualifier("counterService")
    CounterService counterService;

    private Logger log = Logger.getLogger(getClass());

    @Pointcut("within(org.ivanina.course.spring37.cinema.service.BookingService+) execution(* *bookTickets(.))")
    private void bookTickets() {
    }

    @Before("bookTickets()")
    private void bookTicketsCountLog(JoinPoint joinPoint) {
        counterService.add("BookingService.bookTickets(1)");
    }


}

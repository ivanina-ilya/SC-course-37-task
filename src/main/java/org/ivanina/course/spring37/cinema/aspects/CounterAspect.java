package org.ivanina.course.spring37.cinema.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CounterAspect extends Counter {

    private Logger log = Logger.getLogger(CounterAspect.class);

    @Pointcut("within(org.ivanina.course.spring37.cinema.service.EventService+) execution(* *getByName(..))")
    private void accessGetEventByName() { }

    @Before("accessGetEventByName()")
    private void accessGetEventByNameCountLog(JoinPoint joinPoint) {
        log.info("= AOP = Access to EventService.getByName: " + joinPoint.toShortString());
        counterAdd("EventService.getByName");
    }

    @Pointcut("execution(* get*(..)) && target(org.ivanina.course.spring37.cinema.domain.Event)")
    private void accessGetEventBasePrice() { }

    @Before("accessGetEventBasePrice()")
    private void accessGetEventBasePriceLog(JoinPoint joinPoint) {
        log.info("= AOP = Access to Event.getBasePrice: " + joinPoint.toShortString());
        counterAdd("Event.getBasePrice");
    }





}

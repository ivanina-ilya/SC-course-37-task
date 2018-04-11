package org.ivanina.course.spring37.cinema.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class DiscountAspect extends Counter {
    private Logger log = Logger.getLogger(getClass());

    @Pointcut("within(org.ivanina.course.spring37.cinema.service.DiscountService+) execution(* *getDiscountByBirthday(..))")
    private void accessGetDiscountByBirthday(){}

    @Before("accessGetDiscountByBirthday()")
    private void accessGetDiscountByBirthdayCountLog(JoinPoint joinPoint) {
        log.info("= AOP = Access to DiscountService.getDiscountByBirthday: " + joinPoint.toShortString());
        counterAdd("DiscountService.getDiscountByBirthday");
    }

    @Before("within(org.ivanina.course.spring37.cinema.service.DiscountService+) execution(* *getDiscountByCount(..))")
    private void accessGetDiscountByCountLog(JoinPoint joinPoint) {
        log.info("= AOP = Access to DiscountService.getDiscountByCount: " + joinPoint.toShortString());
        counterAdd("DiscountService.getDiscountByCount");
    }

}

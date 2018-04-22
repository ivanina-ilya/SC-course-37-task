package org.ivanina.course.spring37.cinema.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.ivanina.course.spring37.cinema.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;



@Component
@Aspect
public class DiscountAspect {
    @Autowired
    @Qualifier("counterService")
    CounterService counterService;

    private Logger log = Logger.getLogger(getClass());

    @Pointcut("within(org.ivanina.course.spring37.cinema.service.DiscountService+) execution(* *getDiscountByBirthday(..))")
    private void accessGetDiscountByBirthday() {
    }

    @Before("accessGetDiscountByBirthday()")
    private void accessGetDiscountByBirthdayCountLog(JoinPoint joinPoint) {
        counterService.add("DiscountService.getDiscountByBirthday - access");
    }

    @Before("within(org.ivanina.course.spring37.cinema.service.DiscountService+) execution(* *getDiscountByCount(..))")
    private void accessGetDiscountByCountLog(JoinPoint joinPoint) {
        counterService.add("DiscountService.getDiscountByCount - access");
    }


    @AfterReturning(
            pointcut = "within(org.ivanina.course.spring37.cinema.service.DiscountService+) && execution(* *isLuckyWinnerDiscount(..))",
            returning = "returnValue")
    public void loggingRepositoryMethods(JoinPoint joinPoint, Object returnValue) {
        if(returnValue != null &&  (Boolean)returnValue)
            counterService.add("DiscountService.LuckyWinnerDiscount - access");
    }

    /*@AfterReturning(
            pointcut = "within(org.ivanina.course.spring37.cinema.service.DiscountService+) && execution(* *getDiscountByCount(..))",
            returning = "returnValue")
    public void accessGetDiscountByCountLog(JoinPoint joinPoint, Object returnValue) {
        if(returnValue != null &&  (Byte) returnValue > 0 )
            counterService.add("DiscountService.getDiscountByCount");
    }*/

}

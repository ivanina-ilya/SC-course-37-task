package ua.epam.course.spring37.cinema.aspects;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class CounterAspect {
    private Map<String,Integer> counter = new HashMap<>();

    @Autowired
    @Qualifier("log")
    private Logger log;

    @Pointcut("execution(* ua.epam.course.spring37.cinema.service.impl.EventServiceImpl.getByName(String))")
    private void accessGetEventByName() {

    }

    @AfterReturning("accessGetEventByName()")
    private void accessGetEventByNameCountLog(JoinPoint joinPoint) {
        log.info(">> access to Get Event By Name (Event.getByName) with: ");
    }

}

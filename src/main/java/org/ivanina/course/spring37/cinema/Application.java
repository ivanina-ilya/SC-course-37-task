package org.ivanina.course.spring37.cinema;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Application {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationSpringConfig.class);
    }
}

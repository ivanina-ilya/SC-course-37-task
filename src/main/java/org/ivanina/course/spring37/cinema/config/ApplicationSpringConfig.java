package org.ivanina.course.spring37.cinema.config;

import org.apache.log4j.Logger;
import org.ivanina.course.spring37.cinema.service.*;
import org.ivanina.course.spring37.cinema.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;


@Configuration
@ComponentScan({"org.ivanina.course.spring37.cinema.aspects", "org.ivanina.course.spring37.cinema.shell"})
@EnableAspectJAutoProxy
public class ApplicationSpringConfig {

    @Bean(name = "discounts")
    public PropertiesFactoryBean discounts() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("discounts.properties"));
        return bean;
    }

    @Bean(name = "log")
    public Logger logger() {
        return Logger.getLogger("application");
    }

    @Bean(name = "userService")
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean(name = "auditoriumService")
    public AuditoriumService auditoriumService() {
        return new AuditoriumServiceImpl();
    }

    @Bean(name = "eventService")
    public EventService eventService() {
        return new EventServiceImpl();
    }

    @Bean(name = "discountService")
    public DiscountService discountService() {
        return new DiscountServiceImpl();
    }

    @Bean(name = "bookingService")
    public BookingService BookingService() {
        return new BookingServiceImpl();
    }

    @Bean(name = "counterService")
    public CounterService counterService() {
        return new CounterServiceImpl();
    }


}

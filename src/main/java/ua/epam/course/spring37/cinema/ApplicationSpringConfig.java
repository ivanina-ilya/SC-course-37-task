package ua.epam.course.spring37.cinema;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import ua.epam.course.spring37.cinema.service.*;
import ua.epam.course.spring37.cinema.service.impl.*;

@Configuration
public class ApplicationSpringConfig {
    @Bean(name = "auditoriumProps")
    public PropertiesFactoryBean auditoriumProps() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("auditoriums.properties"));
        return bean;
    }
    @Bean(name = "defaultEvensProps")
    public PropertiesFactoryBean defaultEvensProps() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("defaultEvens.properties"));
        return bean;
    }
    @Bean(name = "defaultUsers")
    public PropertiesFactoryBean defaultUsers() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("defaultUsers.properties"));
        return bean;
    }

    @Bean(name = "userService")
    public UserService userService(){
        return new UserServiceImpl();
    }

    @Bean(name = "auditoriumService")
    public AuditoriumService auditoriumService(){
        return new AuditoriumServiceImpl();
    }

    @Bean(name = "eventsService")
    public EventService eventService(){
        return new EventServiceImpl();
    }

    @Bean(name = "discountService")
    public DiscountService discountService(){
        return new DiscountServiceImpl();
    }

    @Bean(name = "bookingService")
    public BookingService BookingService(){
        return new BookingServiceImpl();
    }

}

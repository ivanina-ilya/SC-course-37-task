package ua.epam.course.spring37.cinema;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.epam.course.spring37.cinema.service.AuditoriumService;
import ua.epam.course.spring37.cinema.service.EventService;
import ua.epam.course.spring37.cinema.service.UserService;
import ua.epam.course.spring37.cinema.service.impl.AuditoriumServiceImpl;
import ua.epam.course.spring37.cinema.service.impl.EventServiceImpl;
import ua.epam.course.spring37.cinema.service.impl.UserServiceImpl;


public class Application {
    public static void main(String[] args) {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationSpringConfig.class);

        UserService userService = ctx.getBean("userService", UserServiceImpl.class);
        AuditoriumService auditoriumService = ctx.getBean("auditoriumService", AuditoriumServiceImpl.class);
        EventService eventService = ctx.getBean("eventsService", EventServiceImpl.class);


        System.out.println( "\nUsers:" );
        userService.getAll().forEach(System.out::println);
        System.out.println( "\nAuditoriums:" );
        auditoriumService.getAll().forEach(System.out::println);
        System.out.println( "\nEvents:" );
        eventService.getAll().forEach(event -> {
            System.out.println(event);
            event.getAuditoriums().entrySet().stream()
                    .forEach(entry -> {
                        System.out.println("   "+entry.getKey());
                        System.out.println("   "+entry.getValue());
                        System.out.println();
                    });
        });

        //-------------



        //ctx.close();
    }
}

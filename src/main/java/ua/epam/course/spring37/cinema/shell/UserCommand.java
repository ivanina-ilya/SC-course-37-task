package ua.epam.course.spring37.cinema.shell;

//import org.springframework.shell.standard.ShellMethod;
import ua.epam.course.spring37.cinema.domain.User;
import ua.epam.course.spring37.cinema.service.UserService;

import javax.annotation.Resource;


public class UserCommand  {

    @Resource(name="userService")
    private UserService service;

    //@ShellMethod("Add new user")
    public String addUser(String firstName, String lastName, String email) {
        if(service.getUserByEmail(email) != null) throw new IllegalArgumentException("Duplicate by E-Mail");
        User user = new User(firstName, lastName, email);
        service.save(user);
        return user.toString();
    }
}

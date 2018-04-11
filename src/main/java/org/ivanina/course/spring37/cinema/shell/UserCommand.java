package org.ivanina.course.spring37.cinema.shell;

import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.shell.standard.ShellComponent;
import org.ivanina.course.spring37.cinema.domain.User;
import org.ivanina.course.spring37.cinema.service.UserService;

import javax.annotation.Resource;
import java.util.stream.Collectors;

@ShellComponent
public class UserCommand implements CommandMarker {

    @Resource(name="userService")
    private UserService service;

    @CliAvailabilityIndicator({"addUser","getUserList", "getUserByEmail"})
    public boolean isCommandAvailable() {
        return true;
    }

    @CliCommand(value = "addUser", help = "Add User. Field: firstName, lastName, email. All field are required")
    public String addUser(
            @CliOption(key = { "firstName" }, mandatory = true, help = "firstName") final String firstName,
            @CliOption(key = { "lastName" }, mandatory = true, help = "lastName") final String lastName,
            @CliOption(key = { "email" }, mandatory = true, help = "email") final String email
            ) {
        if(service.getUserByEmail(email) != null) throw new IllegalArgumentException("Duplicate by E-Mail");
        User user = new User(firstName, lastName, email);
        service.save(user);
        return user.toString();
    }

    @CliCommand(value = "getUserList", help = "Show all users")
    public String getUserList() {
        return "\n" + service.getAll().stream()
                .map(user -> user.toString())
                .collect(Collectors.joining("\n-------\n")) +"\n";
    }

    @CliCommand(value = "getUserByEmail", help = "Get User by email if exists")
    public String getUserByEmail(
            @CliOption(key = {"email"}, mandatory = true, help = "E-Mail") final String email
    ){
        return "" + service.getUserByEmail(email) + "\n";
    }
}

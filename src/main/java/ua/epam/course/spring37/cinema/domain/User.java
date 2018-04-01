package ua.epam.course.spring37.cinema.domain;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;

public class User extends DomainObject {

    @NonNull
    private String firstName;

    private String lastName;

    @NonNull
    private String email;

    private LocalDateTime birthday;

    private NavigableSet<Ticket> tickets = new TreeSet<>();

    public User(String firstName, String email) {
        this.firstName = firstName;
        this.email = email;
    }

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public NavigableSet<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(NavigableSet<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void addTickets(Ticket ticket) {
        this.tickets.add(ticket);
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        if (!email.equals(other.email)) { //@NonNull
            return false;
        }
        if (!firstName.equals(other.firstName)) { //@NonNull
            return false;
        }
        if (lastName == null) {
            if (other.lastName != null) {
                return false;
            }
        } else if (!lastName.equals(other.lastName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%s)", firstName, lastName, email);
    }


}

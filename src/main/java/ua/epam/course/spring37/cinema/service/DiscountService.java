package ua.epam.course.spring37.cinema.service;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ua.epam.course.spring37.cinema.domain.Event;
import ua.epam.course.spring37.cinema.domain.User;

import java.time.LocalDateTime;

public interface DiscountService {
    /**
     * Getting discount based on some rules for user that buys some number of
     * tickets for the specific date time of the event
     *
     * @param user
     *            User that buys tickets. Can be <code>null</code>
     * @param event
     *            Event that tickets are bought for
     * @param airDateTime
     *            The date and time event will be aired
     * @param numberOfTickets
     *            Number of tickets that user buys
     * @return discount value from 0 to 100
     */
    byte getDiscount(@Nullable User user, @NonNull Event event, @NonNull LocalDateTime airDateTime, long numberOfTickets);
}

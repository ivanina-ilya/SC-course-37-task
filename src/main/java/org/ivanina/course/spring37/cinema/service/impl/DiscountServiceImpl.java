package org.ivanina.course.spring37.cinema.service.impl;

import org.ivanina.course.spring37.cinema.service.DiscountService;
import org.springframework.lang.Nullable;
import org.ivanina.course.spring37.cinema.domain.Event;
import org.ivanina.course.spring37.cinema.domain.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class DiscountServiceImpl implements DiscountService {

    Integer discountBirthdayInterval = 5;
    Integer discountCountLimit = 10;


    @Override
    public byte getDiscount(@Nullable User user, Event event, LocalDateTime airDateTime, long numberOfTickets) {
        Set<Byte> discountList = new HashSet<>();

        discountList.add(getDiscountByBirthday(user, airDateTime));
        discountList.add(getDiscountByCount(user, numberOfTickets));

        return discountList.stream().max( Byte::compareTo ).orElse((byte)0);
    }

    private byte getDiscountByBirthday(@Nullable User user,LocalDateTime airDateTime){
        if(user.getBirthday() != null &&
                user.getBirthday().isAfter(airDateTime.toLocalDate().minusDays(discountBirthdayInterval)) &&
                user.getBirthday().isBefore(airDateTime.toLocalDate().plusDays(discountBirthdayInterval)) )
            return 5;
        return 0;
    }

    private byte getDiscountByCount(@Nullable User user,long numberOfTickets){
        if(numberOfTickets >= discountCountLimit || (user.getTickets().size()+1) % discountCountLimit == 0 )
            return 50;
        return 0;
    }
}

package org.ivanina.course.spring37.cinema.domain;

import java.math.BigDecimal;

public enum EventRating {
    LOW(new BigDecimal(0.9)),
    MID(new BigDecimal(1.0)),
    HIGH(new BigDecimal(1.2));

    private final BigDecimal coefficient;

    EventRating(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }

}

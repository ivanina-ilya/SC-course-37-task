package org.ivanina.course.spring37.cinema.domain;

public enum EventRating {
    LOW(0.9),
    MID(1.0),
    HIGH(1.2);

    private final double coefficient;
    EventRating(double coefficient){
        this.coefficient = coefficient;
    }
    public double getCoefficient(){
        return coefficient;
    }

}

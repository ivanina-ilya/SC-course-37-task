package org.ivanina.course.spring37.cinema.domain;

public abstract class DomainObject {
    private Long id;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}

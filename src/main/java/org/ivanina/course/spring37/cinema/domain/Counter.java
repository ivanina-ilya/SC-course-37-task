package org.ivanina.course.spring37.cinema.domain;

public class Counter extends DomainObject {
    String key;
    Long count;

    public Counter(Long id, String key, Long count) {
        setId(id);
        this.key = key;
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void add(Long cnt) {
        if (this.count == null) this.count = 0L;
        this.count += cnt;
    }

    @Override
    public String toString() {
        return String.format("Counter >> Count: %d for Key: '%s'", count, key);
    }
}

package org.ivanina.course.spring37.cinema.aspects;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Counter {
    static Map<String, Integer> counter;

    void counterAdd(String name) {
        counterAdd(name, 1);
    }

    void counterAdd(String name, Integer count) {
        if(counter == null) counter = new HashMap<>();
        counter.put(name, counter.getOrDefault(name, 0) + count);
    }

    @Override
    public String toString() {
        return counter.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(" \n"));
    }
}

package org.ivanina.course.spring37.cinema.dao;

import java.util.HashMap;

public class SimpleStorage<T> {
    HashMap<Long, T> store = new HashMap<>();
}

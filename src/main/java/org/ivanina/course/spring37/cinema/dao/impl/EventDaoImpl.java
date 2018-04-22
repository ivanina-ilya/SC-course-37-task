package org.ivanina.course.spring37.cinema.dao.impl;

import org.ivanina.course.spring37.cinema.dao.AuditoriumDao;
import org.ivanina.course.spring37.cinema.dao.EventDao;
import org.ivanina.course.spring37.cinema.domain.Auditorium;
import org.ivanina.course.spring37.cinema.domain.Event;
import org.ivanina.course.spring37.cinema.domain.EventRating;
import org.ivanina.course.spring37.cinema.service.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.lang.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class EventDaoImpl implements EventDao {

    private String table = "events";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("auditoriumDao")
    private AuditoriumDao auditoriumDao;

    @Override
    public Event getByName(String name) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM " + table + " WHERE name=?",
                    new Object[]{name},
                    new RowMapper<Event>() {
                        @Nullable
                        @Override
                        public Event mapRow(ResultSet resultSet, int i) throws SQLException {
                            return EventDaoImpl.mapRow(resultSet);
                        }
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Set<Event> getAll() {
        return new HashSet<>(jdbcTemplate.query("SELECT * FROM  " + table,
                new RowMapper<Event>() {
                    @Nullable
                    @Override
                    public Event mapRow(ResultSet resultSet, int i) throws SQLException {
                        return EventDaoImpl.mapRow(resultSet);
                    }
                }));
    }


    @Override
    public Event get(Long id) {
        try {
            Event event = jdbcTemplate.queryForObject(
                    "SELECT * FROM " + table + " WHERE id=?",
                    new Object[]{id},
                    new RowMapper<Event>() {
                        @Nullable
                        @Override
                        public Event mapRow(ResultSet resultSet, int i) throws SQLException {
                            return EventDaoImpl.mapRow(resultSet);
                        }
                    }
            );
            event.setAuditoriums(getEventAuditoriums(event.getId()));
            return event;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Long save(Event entity) {
        int rows = 0;
        if (entity.getId() == null) {

            GeneratedKeyHolder holder = new GeneratedKeyHolder();
            rows = jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO " + table + " (NAME, EVENTRATING, BASEPRICE, DURATIONMILLISECONDS) VALUES (?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                statement.setString(1, entity.getName());
                Util.statementSetStringOrNull(statement, 2, entity.getRating() != null ? entity.getRating().name() : null);
                Util.statementSetBigDecimalOrNull(statement, 3, entity.getBasePrice());
                Util.statementSetLongOrNull(statement, 4, entity.getDurationMilliseconds());
                return statement;
            }, holder);
            entity.setId(holder.getKey().longValue());
        } else {
            rows = jdbcTemplate.update(
                    "UPDATE " + table + " SET NAME=?, EVENTRATING=?,BASEPRICE=?, DURATIONMILLISECONDS=? WHERE id=?",
                    entity.getName(),
                    entity.getRating() != null ? entity.getRating().name() : null,
                    entity.getBasePrice(),
                    entity.getDurationMilliseconds(),
                    entity.getId());
        }
        removeEventAuditoriums(entity.getId());
        if (entity.getAuditoriums() != null) {
            addEventAuditoriums(entity.getId(), entity.getAuditoriums());
        }
        return rows == 0 ? null : entity.getId();
    }

    @Override
    public Boolean remove(Event entity) {
        if (entity.getId() == null) throw new IllegalArgumentException(
                String.format("Does not exist ID for Event with name %s", entity.getName()));
        if (remove(entity.getId())) {
            entity.setId(null);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean remove(Long id) {
        int rows = jdbcTemplate.update("DELETE FROM " + table + " WHERE id = ? ", id);
        return rows == 0 ? false : true;
    }

    @Override
    public NavigableMap<LocalDateTime, Auditorium> getEventAuditoriums(Long eventId) {
        try {
            return new TreeMap<>(jdbcTemplate.queryForList(
                    "SELECT * FROM EventToAuditorium e " +
                            "RIGHT  JOIN Auditoriums AS a ON a.id = e.auditorium_id " +
                            "WHERE event_id=?",
                    new Object[]{eventId}).stream()
                    .collect(Collectors.toMap(
                            row -> Util.localDateTimeParse(((Map) row).get("airDate").toString()),
                            row -> new Auditorium(
                                    Long.parseLong(((Map) row).get("id").toString()),
                                    ((Map) row).get("name").toString(),
                                    Long.parseLong(((Map) row).get("numberOfSeats") != null ?
                                            ((Map) row).get("numberOfSeats").toString() :
                                            null
                                    ),
                                    Auditorium.vipSeatsParse(((Map) row).get("vipSeats") != null ?
                                            ((Map) row).get("vipSeats").toString() :
                                            null
                                    )
                            )
                    )));

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int removeEventAuditoriums(Long eventId) {
        return jdbcTemplate.update("DELETE FROM EventToAuditorium WHERE event_id = ? ", eventId);
    }


    public void addEventAuditoriums(Long eventId, NavigableMap<LocalDateTime, Auditorium> auditoriums) {
        auditoriums.entrySet().stream()
                .forEach(entry -> jdbcTemplate.update(
                        "INSERT INTO EventToAuditorium (EVENT_ID, AUDITORIUM_ID, AIRDATE) VALUES (?,?,?)",
                        new Object[]{eventId, entry.getValue().getId(), entry.getKey()}));
    }


    @Override
    public Long getNextIncrement() {
        return null;
    }


    public static Event mapRow(ResultSet resultSet) throws SQLException {
        return mapRow(resultSet, new Event(""));
    }

    public static Event mapRow(ResultSet resultSet, Event event) throws SQLException {
        if (resultSet == null) return null;
        if (event == null) event = new Event("");
        event.setId(resultSet.getLong("id"));
        event.setName(resultSet.getString("name"));
        String eventRating = resultSet.getString("eventRating");
        event.setRating(eventRating == null ? null : EventRating.valueOf(eventRating));
        event.setDurationMilliseconds(resultSet.getLong("durationMilliseconds"));
        event.setBasePrice(resultSet.getBigDecimal("basePrice"));

        return event;
    }
}

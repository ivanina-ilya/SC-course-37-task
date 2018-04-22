package org.ivanina.course.spring37.cinema.dao.impl;

import org.ivanina.course.spring37.cinema.dao.EventDao;
import org.ivanina.course.spring37.cinema.dao.TicketDao;
import org.ivanina.course.spring37.cinema.dao.UserDao;
import org.ivanina.course.spring37.cinema.domain.Event;
import org.ivanina.course.spring37.cinema.domain.Ticket;
import org.ivanina.course.spring37.cinema.domain.User;
import org.ivanina.course.spring37.cinema.service.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TicketDaoImpl implements TicketDao {

    private String table = "Tickets";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("eventDao")
    private EventDao eventDao;

    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;

    @Override
    public NavigableSet<Ticket> getTicketsByUser(Long userId) {
        try {
            return new TreeSet<Ticket>(
                    jdbcTemplate.queryForList(
                            "SELECT * FROM  " + table + " WHERE user_id=?",
                            new Object[]{userId}).stream()
                            .map(row -> getTicket((Map) row))
                            .collect(Collectors.toSet()
                            ));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public NavigableSet<Ticket> getTicketsByEvent(Long eventId, LocalDateTime dateTime) {
        try {
            return new TreeSet<Ticket>(
                    jdbcTemplate.queryForList(
                            "SELECT * FROM  " + table + " WHERE EVENT_ID=? AND DATETIME=?",
                            new Object[]{
                                    eventId,
                                    dateTime.withNano(0)
                            }).stream()
                            .map(row -> getTicket((Map) row))
                            .collect(Collectors.toSet()
                            ));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public NavigableSet<Ticket> getTicketsByUserForEvent(Long userId, Long eventId) {
        try {
            return new TreeSet<Ticket>(
                    jdbcTemplate.queryForList(
                            "SELECT * FROM  " + table + " WHERE USER_ID=? AND EVENT_ID=?",
                            new Object[]{
                                    userId,
                                    eventId
                            }).stream()
                            .map(row -> getTicket((Map) row))
                            .collect(Collectors.toSet()
                            ));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public NavigableSet<Ticket> getTicketsByUserForEvent(Long userId, Long eventId, LocalDateTime dateTime) {
        try {
            return new TreeSet<Ticket>(
                    jdbcTemplate.queryForList(
                            "SELECT * FROM  " + table + " WHERE USER_ID=? AND EVENT_ID=? AND DATETIME=?",
                            new Object[]{
                                    userId,
                                    eventId,
                                    dateTime
                            }).stream()
                            .map(row -> getTicket((Map) row))
                            .collect(Collectors.toSet()
                            ));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Set<Ticket> getAll() {
        return new HashSet<>(jdbcTemplate.query("SELECT * FROM  " + table,
                new RowMapper<Ticket>() {
                    @Nullable
                    @Override
                    public Ticket mapRow(ResultSet resultSet, int i) throws SQLException {
                        if (resultSet == null) return null;
                        User user = userDao.get(resultSet.getLong("user_id"));
                        Event event = eventDao.get(resultSet.getLong("event_id"));
                        return getTicket(resultSet);
                    }
                }));
    }

    @Override
    public Ticket get(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM " + table + " WHERE id=?",
                    new Object[]{id},
                    new RowMapper<Ticket>() {
                        @Nullable
                        @Override
                        public Ticket mapRow(ResultSet resultSet, int i) throws SQLException {
                            return getTicket(resultSet);
                        }
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Override
    public Long save(Ticket entity) {
        int rows = 0;
        if (entity.getId() == null) {

            GeneratedKeyHolder holder = new GeneratedKeyHolder();
            rows = jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO " + table + " (user_id, event_id,  dateTime, seat, price) VALUES (?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                Util.statementSetLongOrNull(statement, 1, entity.getUser() != null ? entity.getUser().getId() : null);
                Util.statementSetLongOrNull(statement, 2, entity.getEvent().getId());
                Util.statementSetDateTimeOrNull(statement, 3, entity.getDateTime());
                Util.statementSetLongOrNull(statement, 4, entity.getSeat());
                Util.statementSetBigDecimalOrNull(statement, 5, entity.getPrice());
                return statement;
            }, holder);
            entity.setId(holder.getKey().longValue());
        } else {
            rows = jdbcTemplate.update("UPDATE " + table + " SET user_id=?, event_id=?,dateTime=?, seat=?, price=? WHERE id=?",
                    entity.getUser() != null ? entity.getUser().getId() : null,
                    entity.getEvent().getId(),
                    entity.getDateTime(),
                    entity.getSeat(),
                    entity.getPrice(),
                    entity.getId());
        }
        return rows == 0 ? null : entity.getId();
    }

    @Override
    public Boolean remove(Ticket entity) {
        if (entity.getId() == null) throw new IllegalArgumentException(
                String.format("Does not exist ID for Ticket (User ID: %d, Event ID: %d, Date: %s",
                        entity.getUser() == null ? null : entity.getUser().getId(),
                        entity.getEvent().getId(),
                        entity.getDateTime()));
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
    public Long getNextIncrement() {
        return null;
    }


    private Ticket getTicket(ResultSet resultSet) throws SQLException {
        if (resultSet == null) return null;
        User user = userDao.get(resultSet.getLong("user_id"));
        Event event = eventDao.get(resultSet.getLong("event_id"));
        return new Ticket(
                resultSet.getLong("id"),
                user,
                event,
                Util.localDateTimeParse(resultSet.getString("dateTime")),
                resultSet.getLong("seat"),
                resultSet.getBigDecimal("price")
        );
    }

    private Ticket getTicket(Map row) {
        if (row == null) return null;
        User user = userDao.get(Long.parseLong(row.get("user_id").toString()));
        Event event = eventDao.get(Long.parseLong(row.get("event_id").toString()));
        return new Ticket(
                Long.parseLong(row.get("id").toString()),
                user,
                event,
                Util.localDateTimeParse(row.get("dateTime").toString()),
                row.get("seat") != null ? Long.parseLong(row.get("seat").toString()) : null,
                row.get("price") != null ? new BigDecimal(row.get("price").toString()) : null
        );
    }

}

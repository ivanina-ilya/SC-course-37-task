package org.ivanina.course.spring37.cinema.dao.impl;

import org.ivanina.course.spring37.cinema.dao.AuditoriumDao;
import org.ivanina.course.spring37.cinema.dao.EventDao;
import org.ivanina.course.spring37.cinema.dao.TicketDao;
import org.ivanina.course.spring37.cinema.dao.UserDao;
import org.ivanina.course.spring37.cinema.domain.*;
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
                    "SELECT * FROM  "+table+" WHERE user_id=?",
                    new Object[]{userId}).stream()
                            .map( row -> new Ticket(
                                    Long.parseLong( ((Map)row).get("id").toString()),
                                    ((Map)row).get("user_id") != null ?
                                            userDao.get( Long.parseLong( ((Map)row).get("user_id").toString())) :
                                            null,
                                    eventDao.get( Long.parseLong( ((Map)row).get("event_id").toString()) ),
                                    ((Map)row).get("dateTime") != null ?
                                            Util.localDateTimeParse( ((Map)row).get("dateTime").toString() ) :
                                            null,
                                    ((Map)row).get("seat") != null ?
                                            Long.parseLong( ((Map)row).get("seat").toString() ) :
                                            null,
                                    ((Map)row).get("price") != null ?
                                            Double.parseDouble( ((Map)row).get("price").toString() ) :
                                            null)
                            )
                    .collect(Collectors.toSet()
                    ));
        } catch ( EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public Set<Ticket> getAll() {
        return new HashSet<>(jdbcTemplate.query("SELECT * FROM  "+table,
                new RowMapper<Ticket>(){
                    @Nullable
                    @Override
                    public Ticket mapRow(ResultSet resultSet, int i) throws SQLException {
                        if(resultSet == null ) return null;
                        User user = userDao.get( resultSet.getLong("user_id") );
                        Event event = eventDao.get( resultSet.getLong("event_id") );
                        return getTicket(resultSet);
                    }
                }) );
    }

    @Override
    public Ticket get(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM "+table+" WHERE id=?",
                    new Object[]{id},
                    new RowMapper<Ticket>() {
                        @Nullable
                        @Override
                        public Ticket mapRow(ResultSet resultSet, int i) throws SQLException {
                            return getTicket(resultSet);
                        }
                    }
            );
        } catch ( EmptyResultDataAccessException e){
            return null;
        }
    }


    @Override
    public Long save(Ticket entity) {
        int rows = 0;
        if(entity.getId() == null){

            GeneratedKeyHolder holder = new GeneratedKeyHolder();
            rows = jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO "+table+" (user_id, event_id,  dateTime, seat, price) VALUES (?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                Util.statementSetLongOrNull(statement, 1,entity.getUser() != null ? entity.getUser().getId() : null);
                Util.statementSetLongOrNull(statement, 2,entity.getEvent().getId());
                Util.statementSetDateTimeOrNull(statement, 3,entity.getDateTime());
                Util.statementSetLongOrNull(statement, 4,entity.getSeat());
                Util.statementSetDoubleOrNull(statement, 5,entity.getPrice());
                return statement;
            },holder);
            entity.setId( holder.getKey().longValue() );
        } else {
            rows = jdbcTemplate.update("UPDATE "+table+" SET user_id=?, event_id=?,dateTime=?, seat=?, price=? WHERE id=?",
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
        if(entity.getId() == null) throw new IllegalArgumentException(
                String.format("Does not exist ID for Ticket (User ID: %d, Event ID: %d, Date: %s",
                        entity.getUser() == null ? null : entity.getUser().getId(),
                        entity.getEvent().getId(),
                        entity.getDateTime()) );
        if (remove(entity.getId())){
            entity.setId(null);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean remove(Long id) {
        int rows = jdbcTemplate.update("DELETE from "+table+" WHERE id = ? ", id);
        return rows == 0 ? false : true;
    }

    @Override
    public Long getNextIncrement() {
        return null;
    }


    private Ticket getTicket(ResultSet resultSet) throws SQLException {
        if(resultSet == null ) return null;
        User user = userDao.get( resultSet.getLong("user_id") );
        Event event = eventDao.get( resultSet.getLong("event_id") );
        return new Ticket(
                resultSet.getLong("id"),
                user,
                event,
                Util.localDateTimeParse( resultSet.getString("dateTime") ),
                resultSet.getLong("seat"),
                resultSet.getDouble("price")
        );
    }

}

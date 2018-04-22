package org.ivanina.course.spring37.cinema.dao.impl;

import org.ivanina.course.spring37.cinema.dao.AuditoriumDao;
import org.ivanina.course.spring37.cinema.domain.Auditorium;
import org.ivanina.course.spring37.cinema.service.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.lang.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class AuditoriumDaoImpl implements AuditoriumDao {

    private String table = "Auditoriums";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Auditorium getByName(String name) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM " + table + " WHERE name=?",
                    new Object[]{name},
                    new RowMapper<Auditorium>() {
                        @Nullable
                        @Override
                        public Auditorium mapRow(ResultSet resultSet, int i) throws SQLException {
                            return AuditoriumDaoImpl.mapRow(resultSet);
                        }
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Auditorium get(Long auditoriumId) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM " + table + " WHERE id=?",
                    new Object[]{auditoriumId},
                    new RowMapper<Auditorium>() {
                        @Nullable
                        @Override
                        public Auditorium mapRow(ResultSet resultSet, int i) throws SQLException {
                            return AuditoriumDaoImpl.mapRow(resultSet);
                        }
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public Set<Auditorium> getAll() {
        return new HashSet<>(jdbcTemplate.query("SELECT * FROM  " + table,
                new RowMapper<Auditorium>() {
                    @Nullable
                    @Override
                    public Auditorium mapRow(ResultSet resultSet, int i) throws SQLException {
                        return AuditoriumDaoImpl.mapRow(resultSet);
                    }
                }));
    }


    @Override
    public Long save(Auditorium entity) {
        int rows = 0;
        if (entity.getId() == null) {

            GeneratedKeyHolder holder = new GeneratedKeyHolder();
            rows = jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO " + table + " (name, numberOfSeats, vipSeats) VALUES (?,?,?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                statement.setString(1, entity.getName());
                Util.statementSetLongOrNull(statement, 2, entity.getNumberOfSeats());
                Util.statementSetStringOrNull(statement, 3, entity.vipSeatsToString());
                return statement;
            }, holder);
            entity.setId(holder.getKey().longValue());
        } else {
            rows = jdbcTemplate.update(
                    "UPDATE " + table + " SET name=?, numberOfSeats=?, vipSeats=? WHERE id=?",
                    entity.getName(),
                    entity.getNumberOfSeats(),
                    entity.vipSeatsToString(),
                    entity.getId());
        }
        return rows == 0 ? null : entity.getId();
    }


    @Override
    public Boolean remove(Auditorium entity) {
        if (entity.getId() == null) throw new IllegalArgumentException(
                String.format("Does not exist ID for Auditorium with name %s", entity.getName()));
        return remove(entity.getId());
    }

    @Override
    public Boolean remove(Long id) {
        return jdbcTemplate.update("DELETE FROM " + table + " WHERE id = ? ", id) != 0;
    }

    @Override
    public Long getNextIncrement() {
        return null;
    }


    public static Auditorium mapRow(ResultSet resultSet) throws SQLException {
        return mapRow(resultSet, new Auditorium(""));
    }

    public static Auditorium mapRow(ResultSet resultSet, Auditorium auditorium) throws SQLException {
        if (resultSet == null) return null;
        if (auditorium == null) auditorium = new Auditorium("");

        auditorium.setId(resultSet.getLong("id"));
        auditorium.setName(resultSet.getString("name"));
        auditorium.setNumberOfSeats(resultSet.getLong("numberOfSeats"));
        auditorium.setVipSeats(resultSet.getString("vipSeats") != null ?
                Auditorium.vipSeatsParse(resultSet.getString("vipSeats")) :
                null);
        return auditorium;
    }
}

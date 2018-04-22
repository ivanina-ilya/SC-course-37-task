package org.ivanina.course.spring37.cinema.dao.impl;

import org.ivanina.course.spring37.cinema.dao.CounterDao;
import org.ivanina.course.spring37.cinema.domain.Counter;
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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CounterDaoImpl implements CounterDao {

    private String table = "Counter";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Counter get(String key) {
        return get("key", key);
    }

    @Override
    public Counter get(Long id) {
        return get("id", id);
    }

    private Counter get(String key, Object value) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM " + table + " WHERE " + key + "=?",
                    new Object[]{value},
                    new RowMapper<Counter>() {
                        @Nullable
                        @Override
                        public Counter mapRow(ResultSet resultSet, int i) throws SQLException {
                            return getCounter(resultSet);
                        }
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Set<Counter> getAll() {
        return new HashSet<>(jdbcTemplate.query("SELECT * FROM  " + table,
                new RowMapper<Counter>() {
                    @Nullable
                    @Override
                    public Counter mapRow(ResultSet resultSet, int i) throws SQLException {
                        return getCounter(resultSet);
                    }
                }));
    }

    @Override
    public Map<String, Long> getMap() {
        return getAll().stream()
                .collect(Collectors.toMap(Counter::getKey, Counter::getCount));
    }

    @Override
    public Long save(Counter entity) {
        int rows = 0;
        if (entity.getId() == null) {
            GeneratedKeyHolder holder = new GeneratedKeyHolder();
            rows = jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO " + table + " (KEY, COUNT) VALUES (?,?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                statement.setString(1, entity.getKey());
                statement.setLong(2, entity.getCount());
                return statement;
            }, holder);
            entity.setId(holder.getKey().longValue());
        } else {
            rows = jdbcTemplate.update(
                    "UPDATE " + table + " SET KEY=?, COUNT=? WHERE id=?",
                    entity.getKey(),
                    entity.getCount(),
                    entity.getId());
        }
        return rows == 0 ? null : entity.getId();
    }

    @Override
    public Boolean remove(Counter entity) {
        if (entity.getId() == null) throw new IllegalArgumentException(
                String.format("Does not exist Counter entity with key %s", entity.getKey()));
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


    public Counter getCounter(ResultSet resultSet) throws SQLException {
        return new Counter(
                resultSet.getLong("id"),
                resultSet.getString("key"),
                resultSet.getLong("count"));
    }

}

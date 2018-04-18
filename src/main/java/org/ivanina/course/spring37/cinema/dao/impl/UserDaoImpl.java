package org.ivanina.course.spring37.cinema.dao.impl;

import org.ivanina.course.spring37.cinema.dao.UserDao;
import org.ivanina.course.spring37.cinema.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.lang.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class UserDaoImpl implements UserDao {

    private String table = "users";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Set<User> getAll() {
        return new HashSet<>(jdbcTemplate.query("SELECT * FROM  "+table,
                new RowMapper<User>(){
                    @Nullable
                    @Override
                    public User mapRow(ResultSet resultSet, int i) throws SQLException {
                        return UserDaoImpl.mapRow(resultSet);
                    }
                }) );
    }

    @Override
    public User get(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM "+table+" WHERE id=?",
                new Object[]{id},
                new RowMapper<User>() {
                    @Nullable
                    @Override
                    public User mapRow(ResultSet resultSet, int i) throws SQLException {
                        return UserDaoImpl.mapRow(resultSet);
                    }
                }
        );
    }

    @Override
    public Long save(User entity) {
        int rows = 0;
        if(entity.getId() == null){
            rows = jdbcTemplate.update("INSERT INTO "+table+" (firstName, lastName, email, birthday) VALUES (?,?,?,?)",
                    entity.getFirstName(), entity.getLastName(), entity.getEmail(),
                    java.sql.Timestamp.valueOf(entity.getBirthday()) );

            GeneratedKeyHolder holder = new GeneratedKeyHolder();
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement statement = connection.prepareStatement(
                            "INSERT INTO "+table+" (firstName, lastName, email, birthday) VALUES (?,?,?,?)"
                    );
                    statement.setString(1,entity.getFirstName());
                    statement.setString(2,entity.getLastName());
                    statement.setString(3,entity.getEmail());
                    statement.setDate(4,java.sql.Date.valueOf(entity.getBirthday().toLocalDate()));
                    return statement;
                }
            },holder);
            entity.setId( holder.getKey().longValue() );
        } else {
            rows = jdbcTemplate.update("UPDATE "+table+" SET firstName=?, lastName=?,email=?, birthday=? WHERE id=?",
                    entity.getFirstName(), entity.getLastName(), entity.getEmail(),
                    java.sql.Timestamp.valueOf(entity.getBirthday()),
                    entity.getId());
        }
        return rows == 0 ? null : entity.getId();
    }

    @Override
    public Boolean remove(User entity) {
        if(entity.getId() == null) throw new IllegalArgumentException(
                String.format("Does not exist ID for User with email %s", entity.getEmail()) );
        return remove(entity.getId());
    }

    @Override
    public Boolean remove(Long id) {
        int rows = jdbcTemplate.update("DELETE from ? WHERE id = ? ", table, id);
        return rows == 0 ? false : true;
    }

    @Override
    public Long getNextIncrement() {
        return null;
    }

    public static User mapRow(ResultSet resultSet) throws SQLException {
        return mapRow(resultSet,new User("",""));
    }

    public static User mapRow(ResultSet resultSet, User user) throws SQLException {
        if(resultSet == null ) return null;
        if(user == null) user = new User("","");
        user.setFirstName(resultSet.getString("firstName"));
        user.setLastName(resultSet.getString("lastName"));
        user.setEmail(resultSet.getString("email"));
        user.setBirthday(resultSet.getTimestamp("birthday").toLocalDateTime());
        user.setId( resultSet.getLong("id") );
        return user;
    }
}

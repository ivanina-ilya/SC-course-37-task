package org.ivanina.course.spring37.cinema.service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Map;

public class Util {
    public static void statementSetStringOrNull(PreparedStatement statement, int column, String val) throws SQLException {
        if(val == null){
            statement.setNull(column, Types.VARCHAR);
        } else {
            statement.setString(column,val);
        }
    }

    public static void statementSetLongOrNull(PreparedStatement statement, int column, Long val) throws SQLException {
        if(val == null){
            statement.setNull(column, Types.BIGINT);
        } else {
            statement.setLong(column,val);
        }
    }

    public static void statementSetDoubleOrNull(PreparedStatement statement, int column, Double val) throws SQLException {
        if(val == null){
            statement.setNull(column, Types.DOUBLE);
        } else {
            statement.setDouble(column,val);
        }
    }

    public static void statementSetDateOrNull(PreparedStatement statement, int column, Date val) throws SQLException {
        if(val == null){
            statement.setNull(column, Types.DATE);
        } else {
            statement.setDate(column,val);
        }
    }

    public static void statementSetDateTimeOrNull(PreparedStatement statement, int column, LocalDateTime val) throws SQLException {
        if(val == null){
            statement.setNull(column, Types.DATE);
        } else {
            statement.setTimestamp(column, java.sql.Timestamp.valueOf(val));
        }
    }

    public static LocalDateTime localDateTimeParse(String dateTimeString){
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .optionalStart()
                .appendPattern(".")
                .appendFraction(ChronoField.MICRO_OF_SECOND, 1 ,6, false)
                .optionalEnd()
                .toFormatter();
        return LocalDateTime.parse( dateTimeString, formatter );
    }
}

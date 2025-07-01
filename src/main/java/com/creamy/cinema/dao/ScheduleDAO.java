package com.creamy.cinema.dao;

import com.creamy.cinema.models.Schedule;
import com.creamy.cinema.util.CinemaException;
import com.creamy.cinema.util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {
    public static void createSchedule(DBConnection connection, Schedule schedule) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareInsertStatement("INSERT INTO schedule (movie_id, hall_id, start_datetime, end_datetime) VALUES (?, ?, ?, ?)");

            statement.setInt(1, schedule.getMovieId());
            statement.setInt(2, schedule.getHallId());
            statement.setTimestamp(3, Timestamp.valueOf(schedule.getStartDatetime()));
            statement.setTimestamp(4, Timestamp.valueOf(schedule.getEndDatetime()));

            int scheduleId = connection.executeInsertStatement(statement);
            schedule.setScheduleId(scheduleId);
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to create schedule.", e);
        }
    }

    public static List<Schedule> requestSchedules(DBConnection connection) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM schedule WHERE deleted=0");
            ArrayList<Schedule> schedules = new ArrayList<>();
            ResultSet resultSet = connection.executeStatement(statement);
            while (resultSet.next()) {
                schedules.add(getScheduleFromResultSet(resultSet));
            }
            return schedules;
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to request schedule.", e);
        }
    }

    public static Schedule requestScheduleByScheduleId(DBConnection connection, int scheduleId) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM schedule WHERE schedule_id=?");
            statement.setInt(1, scheduleId);

            ResultSet resultSet = connection.executeStatement(statement);
            if (resultSet.next()) {
                return getScheduleFromResultSet(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to request schedule.", e);
        }
    }

    public static boolean requestScheduleAvailability(DBConnection connection, Schedule schedule) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM schedule WHERE hall_id=? AND start_datetime < ? AND end_datetime > ? AND deleted=0");
            statement.setInt(1, schedule.getHallId());
            statement.setTimestamp(2, Timestamp.valueOf(schedule.getEndDatetime()));
            statement.setTimestamp(3, Timestamp.valueOf(schedule.getStartDatetime()));
            ResultSet resultSet = connection.executeStatement(statement);

            if (resultSet.next()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to request schedule.", e);
        }
    }

    public static boolean requestScheduleUpdateAvailability(DBConnection connection, Schedule schedule) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM schedule WHERE schedule_id!=? AND hall_id=? AND start_datetime < ? AND end_datetime > ? AND deleted=0");
            statement.setInt(1, schedule.getScheduleId());
            statement.setInt(2, schedule.getHallId());
            statement.setTimestamp(3, Timestamp.valueOf(schedule.getEndDatetime()));
            statement.setTimestamp(4, Timestamp.valueOf(schedule.getStartDatetime()));
            ResultSet resultSet = connection.executeStatement(statement);

            if (resultSet.next()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to request schedule.", e);
        }
    }

    public static boolean updateSchedule(DBConnection connection, Schedule schedule) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE schedule SET movie_id=?, hall_id=?, start_datetime=?, end_datetime=?, deleted=? WHERE schedule_id=?");
            statement.setInt(1, schedule.getMovieId());
            statement.setInt(2, schedule.getHallId());
            statement.setTimestamp(3, Timestamp.valueOf(schedule.getStartDatetime()));
            statement.setTimestamp(4, Timestamp.valueOf(schedule.getEndDatetime()));
            statement.setBoolean(5, schedule.isDeleted());
            statement.setInt(6, schedule.getScheduleId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to update schedule.", e);
        }
    }

    private static Schedule getScheduleFromResultSet(ResultSet resultSet) throws SQLException {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(resultSet.getInt("schedule_id"));
        schedule.setMovieId(resultSet.getInt("movie_id"));
        schedule.setHallId(resultSet.getInt("hall_id"));
        schedule.setStartDatetime(resultSet.getTimestamp("start_datetime").toLocalDateTime());
        schedule.setEndDatetime(resultSet.getTimestamp("end_datetime").toLocalDateTime());
        schedule.setDeleted(resultSet.getBoolean("deleted"));
        return schedule;
    }
}

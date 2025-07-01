package com.creamy.cinema.dao;

import com.creamy.cinema.models.Hall;
import com.creamy.cinema.util.CinemaException;
import com.creamy.cinema.util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HallDAO {
    public static void createHall(DBConnection connection, Hall hall) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareInsertStatement("INSERT INTO hall (name, type, hall_rows, seat_per_row) VALUES (?, ?, ?, ?)");

            statement.setString(1, hall.getHallName());
            statement.setString(2, hall.getHallType());
            statement.setInt(3, hall.getRows());
            statement.setInt(4, hall.getSeatPerRow());

            int hallId = connection.executeInsertStatement(statement);
            hall.setHallId(hallId);
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to create hall.", e);
        }
    }

    public static List<Hall> requestHalls(DBConnection connection) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM hall WHERE deleted=0");
            ArrayList<Hall> halls = new ArrayList<>();
            ResultSet resultSet = connection.executeStatement(statement);
            while (resultSet.next()) {
                halls.add(getHallFromResultSet(resultSet));
            }
            return halls;
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to request hall.", e);
        }
    }

    public static Hall requestHallByHallId(DBConnection connection, int hallId) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM hall WHERE hall_id=?");
            statement.setInt(1, hallId);

            ResultSet resultSet = connection.executeStatement(statement);
            if (resultSet.next()) {
                return getHallFromResultSet(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to request hall.", e);
        }
    }

    public static List<String> requestAllSeat(DBConnection connection, int hallId) throws CinemaException {
        List<String> seats = new ArrayList<>();
        Hall hall = HallDAO.requestHallByHallId(connection, hallId);
        for (int i = 0; i < hall.getRows(); i++) {
            String label = toRowLabel(i);
            for (int j = 0; j < hall.getSeatPerRow(); j++) {
                seats.add(label + j);
            }
        }
        return seats;
    }

    public static boolean updateHall(DBConnection connection, Hall hall) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE hall SET name=?, type=?, hall_rows=?, seat_per_row=?, deleted=? WHERE hall_id=?");
            statement.setString(1, hall.getHallName());
            statement.setString(2, hall.getHallType());
            statement.setInt(3, hall.getRows());
            statement.setInt(4, hall.getSeatPerRow());
            statement.setBoolean(5, hall.isDeleted());
            statement.setInt(6, hall.getHallId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to update hall.", e);
        }
    }

    private static Hall getHallFromResultSet(ResultSet resultSet) throws SQLException {
        Hall hall = new Hall();
        hall.setHallId(resultSet.getInt("hall_id"));
        hall.setHallName(resultSet.getString("name"));
        hall.setHallType(resultSet.getString("type"));
        hall.setRows(resultSet.getInt("hall_rows"));
        hall.setSeatPerRow(resultSet.getInt("seat_per_row"));
        hall.setDeleted(resultSet.getBoolean("deleted"));
        return hall;
    }

    public static String toRowLabel(int index) {
        StringBuilder label = new StringBuilder();
        index++; // 1-based for calculation
        while (index > 0) {
            index--; // shift to 0-based
            char c = (char) ('A' + (index % 26));
            label.insert(0, c);
            index /= 26;
        }
        return label.toString();
    }
}

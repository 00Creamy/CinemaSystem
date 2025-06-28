package com.creamy.cinema.dao;

import com.creamy.cinema.models.Hall;
import com.creamy.cinema.models.Movie;
import com.creamy.cinema.util.CinemaException;
import com.creamy.cinema.util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MovieDAO {
    public static void createMovie(DBConnection connection, Movie movie) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareInsertStatement("INSERT INTO movie (name, rows, seat_per_row) VALUES (?, ?, ?)");

            statement.setString(1, hall.getHallName());
            statement.setInt(2, hall.getRows());
            statement.setInt(3, hall.getSeatPerRow());

            int hallId = connection.executeInsertStatement(statement);
            hall.setHallId(hallId);
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to create hall.", e);
        }
    }
}

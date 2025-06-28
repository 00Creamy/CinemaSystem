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
            PreparedStatement statement = connection.prepareInsertStatement("INSERT INTO movie (title, description, duration, imagePath, ) VALUES (?, ?, ?)");
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to create hall.", e);
        }
    }
}

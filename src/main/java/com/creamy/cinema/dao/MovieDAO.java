package com.creamy.cinema.dao;

import com.creamy.cinema.models.Actor;
import com.creamy.cinema.models.Movie;
import com.creamy.cinema.util.CinemaException;
import com.creamy.cinema.util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
    public static void createMovie(DBConnection connection, Movie movie) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareInsertStatement("INSERT INTO movie (title, description, duration, image_path, rating, language, status) VALUES (?, ?, ?, ?, ?, ?, ?)");

            statement.setString(1, movie.getTitle());
            statement.setString(2, movie.getDescription());
            statement.setInt(3, (int) movie.getDuration().getSeconds());
            statement.setString(4, movie.getImagePath());
            statement.setString(5, movie.getRating());
            statement.setString(6, movie.getLanguage());
            statement.setInt(7, movie.getStatus().getLevel());

            int createdId = connection.executeInsertStatement(statement);

            for (String tag: movie.getTags()) {
                statement = connection.prepareStatement("INSERT INTO movie_tag (movie_id, tag) VALUES (?, ?)");
                statement.setInt(1, movie.getMovieId());
                statement.setString(2, tag);
                statement.executeUpdate();
            }

            for (String director: movie.getDirectors()) {
                statement = connection.prepareStatement("INSERT INTO movie_director (movie_id, director) VALUES (?, ?)");
                statement.setInt(1, movie.getMovieId());
                statement.setString(2, director);
                statement.executeUpdate();
            }

            for (Actor actor: movie.getActors()) {
                statement = connection.prepareStatement("INSERT INTO movie_tag (movie_id, name, role) VALUES (?, ?, ?)");
                statement.setInt(1, movie.getMovieId());
                statement.setString(2, actor.getName());
                statement.setString(3, actor.getRole());
                statement.executeUpdate();
            }

            for (String subtitle: movie.getSubtitles()) {
                statement = connection.prepareStatement("INSERT INTO movie_subtitle (movie_id, subtitle) VALUES (?, ?)");
                statement.setInt(1, movie.getMovieId());
                statement.setString(2, subtitle);
                statement.executeUpdate();
            }

            movie.setMovieId(createdId);
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to create movie.", e);
        }
    }

    public static List<Movie> requestMovies(DBConnection connection) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM movie WHERE deleted=0");
            ArrayList<Movie> movies = new ArrayList<>();

            ResultSet resultSet = connection.executeStatement(statement);
            while (resultSet.next()) {
                movies.add(getMovieFromResultSet(resultSet));
            }

            for (Movie movie: movies) {
                getMovieData(connection, movie);
            }

            return movies;
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to request movie.", e);
        }
    }

    public static Movie requestMovieByMovieId(DBConnection connection, int movieId) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM movie WHERE movie_id=?");
            statement.setInt(1, movieId);

            ResultSet resultSet = connection.executeStatement(statement);
            if (resultSet.next()) {
                Movie movie = getMovieFromResultSet(resultSet);
                getMovieData(connection, movie);
                return movie;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to request movie.", e);
        }
    }

    public static boolean updateMovie(DBConnection connection, Movie movie) throws CinemaException {
        try {
            Movie original = requestMovieByMovieId(connection, movie.getMovieId());
            PreparedStatement statement = connection.prepareStatement("UPDATE movie SET title=?, description=?, duration=?, image_path=?, rating=?, language=?, status=?, deleted=? WHERE movie_id=?");

            statement.setString(1, movie.getTitle());
            statement.setString(2, movie.getDescription());
            statement.setInt(3, (int) movie.getDuration().getSeconds());
            statement.setString(4, movie.getImagePath());
            statement.setString(5, movie.getRating());
            statement.setString(6, movie.getLanguage());
            statement.setInt(7, movie.getStatus().getLevel());
            statement.setBoolean(8, movie.isDeleted());
            statement.setInt(9, movie.getMovieId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                List<String> added = new ArrayList<>(movie.getTags());
                added.removeAll(original.getTags());
                List<String> removed = new ArrayList<>(original.getTags());
                removed.removeAll(movie.getTags());

                for (String add: added) {
                    statement = connection.prepareStatement("INSERT INTO movie_subtitle (movie_id, subtitle) VALUES (?, ?)");
                    statement.setInt(1, movie.getMovieId());
                    statement.setString(2, add);
                    statement.executeUpdate();
                }

                for (String remove: removed) {
                    statement = connection.prepareStatement("DELETE FROM movie_subtitle WHERE movie_id=?, subtitle=?");
                    statement.setInt(1, movie.getMovieId());
                    statement.setString(2, remove);
                    statement.executeUpdate();
                }

                added = new ArrayList<>(movie.getDirectors());
                added.removeAll(original.getDirectors());
                removed = new ArrayList<>(original.getDirectors());
                removed.removeAll(movie.getDirectors());

                for (String add: added) {
                    statement = connection.prepareStatement("INSERT INTO movie_director (movie_id, director) VALUES (?, ?)");
                    statement.setInt(1, movie.getMovieId());
                    statement.setString(2, add);
                    statement.executeUpdate();
                }

                for (String remove: removed) {
                    statement = connection.prepareStatement("DELETE FROM movie_director WHERE movie_id=?, director=?");
                    statement.setInt(1, movie.getMovieId());
                    statement.setString(2, remove);
                    statement.executeUpdate();
                }

                List<Actor> addedActors = new ArrayList<>(movie.getActors());
                addedActors.removeAll(original.getActors());
                List<Actor> removedActors = new ArrayList<>(original.getActors());
                removedActors.removeAll(movie.getActors());

                for (Actor add: addedActors) {
                    statement = connection.prepareStatement("INSERT INTO movie_actor (movie_id, name, role) VALUES (?, ?, ?)");
                    statement.setInt(1, movie.getMovieId());
                    statement.setString(2, add.getName());
                    statement.setString(3, add.getRole());
                    statement.executeUpdate();
                }

                for (Actor remove: removedActors) {
                    statement = connection.prepareStatement("DELETE FROM movie_director WHERE movie_id=?, name=?, role=?");
                    statement.setInt(1, movie.getMovieId());
                    statement.setString(2, remove.getName());
                    statement.setString(3, remove.getRole());
                    statement.executeUpdate();
                }

                added = new ArrayList<>(movie.getSubtitles());
                added.removeAll(original.getSubtitles());
                removed = new ArrayList<>(original.getSubtitles());
                removed.removeAll(movie.getSubtitles());

                for (String add: added) {
                    statement = connection.prepareStatement("INSERT INTO movie_subtitle (movie_id, subtitle) VALUES (?, ?)");
                    statement.setInt(1, movie.getMovieId());
                    statement.setString(2, add);
                    statement.executeUpdate();
                }

                for (String remove: removed) {
                    statement = connection.prepareStatement("DELETE FROM movie_subtitle WHERE movie_id=?, subtitle=?");
                    statement.setInt(1, movie.getMovieId());
                    statement.setString(2, remove);
                    statement.executeUpdate();
                }
            }

            return affectedRows > 0;
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to request movie.", e);
        }
    }

    private static Movie getMovieFromResultSet(ResultSet resultSet) throws SQLException {
        Movie movie = new Movie();

        movie.setMovieId(resultSet.getInt("movie_id"));
        movie.setTitle(resultSet.getString("title"));
        movie.setDescription(resultSet.getString("description"));
        movie.setDuration(Duration.ofSeconds(resultSet.getInt("duration")));
        movie.setImagePath(resultSet.getString("image_path"));
        movie.setRating(resultSet.getString("rating"));
        movie.setLanguage(resultSet.getString("language"));
        movie.setStatus(Movie.MovieStatus.getStatusFromLevel(resultSet.getInt("status")));
        movie.setDeleted(resultSet.getBoolean("deleted"));

        return movie;
    }

    private static void getMovieData(DBConnection connection, Movie movie) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM movie_tag WHERE movie_id=?");
        statement.setInt(1, movie.getMovieId());
        ResultSet resultSet = connection.executeStatement(statement);

        while (resultSet.next()) {
            movie.getTags().add(resultSet.getString("tag"));
        }

        statement = connection.prepareStatement("SELECT * FROM movie_director WHERE movie_id=?");
        statement.setInt(1, movie.getMovieId());
        resultSet = connection.executeStatement(statement);

        while (resultSet.next()) {
            movie.getDirectors().add(resultSet.getString("director"));
        }

        statement = connection.prepareStatement("SELECT * FROM movie_actor WHERE movie_id=?");
        statement.setInt(1, movie.getMovieId());
        resultSet = connection.executeStatement(statement);

        while (resultSet.next()) {
            Actor actor = new Actor();
            actor.setName(resultSet.getString("name"));
            actor.setRole(resultSet.getString("role"));
            movie.getActors().add(actor);
        }

        statement = connection.prepareStatement("SELECT * FROM movie_subtitle WHERE movie_id=?");
        statement.setInt(1, movie.getMovieId());
        resultSet = connection.executeStatement(statement);

        while (resultSet.next()) {
            movie.getDirectors().add(resultSet.getString("subtitle"));
        }
    }
}

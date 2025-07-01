package com.creamy.cinema.servlet;

import com.creamy.cinema.dao.MovieDAO;
import com.creamy.cinema.models.Movie;
import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MoviesList extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = getUserFromSession(request, response);
            if (user != null) {
                String movieIdInput = request.getParameter("id");
                if (verifyUser(request, user) && user.getAccessLevel().getLevel() >= User.AccessLevel.STAFF.getLevel()) {
                    if (movieIdInput != null) {
                        Movie movie = MovieDAO.requestMovieByMovieId(connection, Integer.parseInt(movieIdInput));
                        request.setAttribute("movie", movie);
                        forward(request, response, "/WEB-INF/MoviesView.jsp");
                        return;
                    }
                    List<Movie> movies = MovieDAO.requestMovies(connection);
                    request.setAttribute("movies", movies);
                } else {
                    if (movieIdInput != null) {
                        Movie movie = MovieDAO.requestMovieByMovieId(connection, Integer.parseInt(movieIdInput));
                        request.setAttribute("movie", movie);
                        forward(request, response, "/WEB-INF/Customer/MoviesView.jsp");
                        return;
                    }
                    List<Movie> movies = MovieDAO.requestMovies(connection).stream().filter(movie -> movie.getStatus() != Movie.MovieStatus.ARCHIVED).toList();
                    request.setAttribute("movies", movies);
                }
            }
            forward(request, response);
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), ".");
        } catch (NumberFormatException e) {
            printErrorRedirect(response.getWriter(), "Invalid id.", "Movies");
        }
    }

    @Override
    protected String getForwardPath() {
        return "/WEB-INF/Movies.jsp";
    }
}

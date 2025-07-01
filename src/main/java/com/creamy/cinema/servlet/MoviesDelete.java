package com.creamy.cinema.servlet;

import com.creamy.cinema.dao.HallDAO;
import com.creamy.cinema.dao.MovieDAO;
import com.creamy.cinema.models.Hall;
import com.creamy.cinema.models.Movie;
import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MoviesDelete extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response,2);
            if (user != null) {
                String idInput = request.getParameter("id");
                if (idInput != null) {
                    int id = Integer.parseInt(idInput);
                    Movie deleteMovie = MovieDAO.requestMovieByMovieId(connection, id);
                    if (deleteMovie != null) {
                        deleteMovie.setDeleted(true);
                        MovieDAO.updateMovie(connection, deleteMovie);
                        response.sendRedirect("Movies");
                    } else {
                        printErrorRedirect(response.getWriter(), "Invalid id.", "Movies");
                    }
                } else {
                    printErrorRedirect(response.getWriter(), "Invalid id.", "Movies");
                }
            }
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), "Movies");
        }
    }
}

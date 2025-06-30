package com.creamy.cinema.servlet;

import com.creamy.cinema.dao.MovieDAO;
import com.creamy.cinema.models.Movie;
import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Homepage extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = getUserFromSession(request, response);
            if (user != null) {
                if (verifyUser(request, user) && user.getAccessLevel().getLevel() >= User.AccessLevel.STAFF.getLevel()) {
                    forward(request, response, "/WEB-INF/Dashboard.jsp");
                    return;
                }
            }
            request.setAttribute("movies", MovieDAO.requestMovies(connection).stream().filter(movie -> movie.getStatus() == Movie.MovieStatus.AIRING).toList());
            forward(request, response);
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(),  e.getMessage(), ".");
        }
    }

    @Override
    protected String getForwardPath() {
        return "/WEB-INF/Homepage.jsp";
    }
}

package com.creamy.cinema.servlet;

import com.creamy.cinema.dao.HallDAO;
import com.creamy.cinema.dao.MovieDAO;
import com.creamy.cinema.dao.ScheduleDAO;
import com.creamy.cinema.models.Hall;
import com.creamy.cinema.models.Movie;
import com.creamy.cinema.models.Schedule;
import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SchedulesList extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = getUserFromSession(request, response);
            if (user != null) {
                if (verifyUser(request, user) && user.getAccessLevel().getLevel() >= User.AccessLevel.MANAGER.getLevel()) {
                    List<Schedule> schedules = ScheduleDAO.requestSchedules(connection);
                    Map<Integer, Movie> movies = MovieDAO.requestMovies(connection).stream().collect(Collectors.toMap(Movie::getMovieId, movie -> movie));
                    Map<Integer, Hall> halls = HallDAO.requestHalls(connection).stream().collect(Collectors.toMap(Hall::getHallId, hall -> hall));
                    request.setAttribute("schedules", schedules);
                    request.setAttribute("movies", movies);
                    request.setAttribute("halls", halls);
                } else {
                    List<Schedule> schedules = ScheduleDAO.requestSchedules(connection).stream().filter(Schedule::isActive).toList();
                    Map<Integer, Movie> movies = MovieDAO.requestMovies(connection).stream().collect(Collectors.toMap(Movie::getMovieId, movie -> movie));
                    Map<Integer, Hall> halls = HallDAO.requestHalls(connection).stream().collect(Collectors.toMap(Hall::getHallId, hall -> hall));
                    request.setAttribute("schedules", schedules);
                    request.setAttribute("movies", movies);
                    request.setAttribute("halls", halls);
                }
            }
            forward(request, response);
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), ".");
        }
    }

    @Override
    protected String getForwardPath() {
        return "/WEB-INF/Schedules.jsp";
    }
}

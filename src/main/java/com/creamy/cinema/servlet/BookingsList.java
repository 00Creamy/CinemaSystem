package com.creamy.cinema.servlet;

import com.creamy.cinema.dao.BookingDAO;
import com.creamy.cinema.dao.HallDAO;
import com.creamy.cinema.dao.MovieDAO;
import com.creamy.cinema.dao.ScheduleDAO;
import com.creamy.cinema.models.*;
import com.creamy.cinema.util.CinemaException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookingsList extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response, 1);
            if (user != null) {
                List<Booking> bookings = BookingDAO.requestBookings(connection);
                Map<Integer, Schedule> schedules = ScheduleDAO.requestSchedules(connection).stream().collect(Collectors.toMap(Schedule::getScheduleId, schedule -> schedule));
                Map<Integer, Movie> movies = MovieDAO.requestMovies(connection).stream().collect(Collectors.toMap(Movie::getMovieId, movie -> movie));
                Map<Integer, Hall> halls = HallDAO.requestHalls(connection).stream().collect(Collectors.toMap(Hall::getHallId, hall -> hall));
                request.setAttribute("bookings", bookings);
                request.setAttribute("schedules", schedules);
                request.setAttribute("movies", movies);
                request.setAttribute("halls", halls);
                forward(request, response);
            }
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), ".");
        }
    }

    @Override
    protected String getForwardPath() {
        return "/WEB-INF/Bookings.jsp";
    }
}

package com.creamy.cinema.servlet;

import com.creamy.cinema.dao.HallDAO;
import com.creamy.cinema.dao.MovieDAO;
import com.creamy.cinema.dao.ScheduleDAO;
import com.creamy.cinema.models.Movie;
import com.creamy.cinema.models.Schedule;
import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class SchedulesCreate extends BaseServlet {
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("movies", MovieDAO.requestMovies(connection).stream().filter(movie -> movie.getStatus() != Movie.MovieStatus.ARCHIVED).toList());
            request.setAttribute("halls", HallDAO.requestHalls(connection).stream().filter(hall -> !hall.isDeleted()).toList());
            forward(request, response);
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), ".");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response,2);
            if (user != null) {
                processRequest(request, response);
            }
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), ".");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response,2);
            if (user != null) {
                String movieIdInput = request.getParameter("movieId");
                String hallIdInput = request.getParameter("hallId");
                // Use input type="datetime-local"
                String startDatetimeInput = request.getParameter("startDatetime");
                String endDatetimeInput = request.getParameter("endDatetime");

                if (isStringValid(movieIdInput, hallIdInput, startDatetimeInput, endDatetimeInput)) {
                    int movieId = Integer.parseInt(movieIdInput);
                    int hallId = Integer.parseInt(hallIdInput);
                    LocalDateTime startDatetime = LocalDateTime.parse(startDatetimeInput);
                    LocalDateTime endDatetime = LocalDateTime.parse(endDatetimeInput);

                    Schedule schedule = new Schedule();
                    schedule.setMovieId(movieId);
                    schedule.setHallId(hallId);
                    schedule.setStartDatetime(startDatetime);
                    schedule.setEndDatetime(endDatetime);

                    if (ScheduleDAO.requestScheduleAvailability(connection, schedule)) {
                        ScheduleDAO.createSchedule(connection, schedule);
                        response.sendRedirect("Schedules");
                        return;
                    } else {
                        request.setAttribute("error", "Schdule time is booked.");
                    }
                } else {
                    request.setAttribute("error", "All fields is required.");
                }
                processRequest(request, response);
            }
        } catch (CinemaException e) {
            request.setAttribute("error", e.getMessage());
            processRequest(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Movie or Hall is invalid");
            processRequest(request, response);
        } catch (DateTimeParseException e) {
            request.setAttribute("error", "Invalid datetime.");
            processRequest(request, response);
        }
    }

    @Override
    protected String getForwardPath() {
        return "/WEB-INF/SchedulesAdd.jsp";
    }
}

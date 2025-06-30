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
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class SchedulesUpdate extends BaseServlet {
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
                String idInput = request.getParameter("id");
                if (idInput != null) {
                    int id = Integer.parseInt(idInput);
                    Schedule updateSchedule = ScheduleDAO.requestScheduleByScheduleId(connection, id);
                    if (updateSchedule != null) {
                        request.setAttribute("updateSchedule", updateSchedule);
                        processRequest(request, response);
                    } else {
                        printErrorRedirect(response.getWriter(), "Invalid id.", "Schedules");
                    }
                } else {
                    printErrorRedirect(response.getWriter(), "Invalid id.", "Schedules");
                }
            }
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), "Schedules");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response,2);
            if (user != null) {
                String idInput = request.getParameter("id");
                String movieIdInput = request.getParameter("movieId");
                String hallIdInput = request.getParameter("hallId");
                // Use input type="datetime-local"
                String startDatetimeInput = request.getParameter("startDatetime");
                String endDatetimeInput = request.getParameter("endDatetime");

                if (idInput != null) {
                    int id = Integer.parseInt(idInput);
                    Schedule updateSchedule = ScheduleDAO.requestScheduleByScheduleId(connection, id);

                    if (updateSchedule != null) {
                        try {
                            if (isStringValid(movieIdInput, hallIdInput, startDatetimeInput, endDatetimeInput)) {
                                int movieId = Integer.parseInt(movieIdInput);
                                int hallId = Integer.parseInt(hallIdInput);
                                LocalDateTime startDatetime = LocalDateTime.parse(startDatetimeInput);
                                LocalDateTime endDatetime = LocalDateTime.parse(endDatetimeInput);

                                updateSchedule.setMovieId(movieId);
                                updateSchedule.setHallId(hallId);
                                updateSchedule.setStartDatetime(startDatetime);
                                updateSchedule.setEndDatetime(endDatetime);

                                if (ScheduleDAO.requestScheduleUpdateAvailability(connection, updateSchedule)) {
                                    ScheduleDAO.updateSchedule(connection, updateSchedule);
                                    response.sendRedirect("Schedules");
                                    return;
                                } else {
                                    request.setAttribute("error", "Schdule time is booked.");
                                }
                            } else {
                                request.setAttribute("error", "All fields is required.");
                            }
                        } catch (CinemaException e) {
                            request.setAttribute("error", e.getMessage());
                        } catch (NumberFormatException e) {
                            request.setAttribute("error", "Movie or Hall is invalid");
                        } catch (DateTimeParseException e) {
                            request.setAttribute("error", "Invalid datetime.");
                        }
                        request.setAttribute("updateSchedule", updateSchedule);
                        processRequest(request, response);
                    } else {
                        printErrorRedirect(response.getWriter(), "Invalid Id.", "Schedules");
                    }
                } else {
                    printErrorRedirect(response.getWriter(), "Invalid Id.", "Schedules");
                }
            }
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), "Schedules");
        } catch (NumberFormatException e) {
            printErrorRedirect(response.getWriter(), "Invalid id.", "Schedules");
        }
    }

    @Override
    protected String getForwardPath() {
        return "/WEB-INF/SchedulesUpdate.jsp";
    }
}

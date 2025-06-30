package com.creamy.cinema.servlet;

import com.creamy.cinema.dao.HallDAO;
import com.creamy.cinema.dao.MovieDAO;
import com.creamy.cinema.models.Actor;
import com.creamy.cinema.models.Hall;
import com.creamy.cinema.models.Movie;
import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

public class MoviesUpdate extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response,2);
            if (user != null) {
                String idInput = request.getParameter("id");
                if (idInput != null) {
                    int id = Integer.parseInt(idInput);
                    Movie updateMovie = MovieDAO.requestMovieByMovieId(connection, id);
                    if (updateMovie != null) {
                        request.setAttribute("updateMovie", updateMovie);
                        forward(request, response);
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response,2);
            if (user != null) {
                String idInput = request.getParameter("id");
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                String hoursInput = request.getParameter("hours");
                String minsInput = request.getParameter("mins");
                String secondsInput = request.getParameter("seconds");
                String imagePath = "Unimplemented";
                String tagsInput = request.getParameter("tags");
                String directorsInput = request.getParameter("directors");
                String actorsInput = request.getParameter("actors");
                String rating = request.getParameter("rating");
                String language = request.getParameter("language");
                String subtitlesInput = request.getParameter("subtitles");
                String statusInput = request.getParameter("status");

                if (idInput != null) {
                    int id = Integer.parseInt(idInput);
                    Movie updateMovie = MovieDAO.requestMovieByMovieId(connection, id);

                    if (updateMovie != null) {
                        try {
                            if (isStringValid(title, description, hoursInput, minsInput, secondsInput, imagePath, tagsInput, directorsInput, actorsInput, rating, language, subtitlesInput, statusInput)) {
                                int hours = Integer.parseInt(hoursInput);
                                int mins = Integer.parseInt(minsInput);
                                int seconds = Integer.parseInt(secondsInput);
                                Duration duration = Duration.ofHours(hours).plusMinutes(mins).plusSeconds(seconds);
                                List<String> tags = Stream.of(tagsInput.split(",")).filter(s -> !s.isBlank()).toList();
                                List<String> directors = Stream.of(tagsInput.split(",")).filter(s -> !s.isBlank()).toList();
                                List<Actor> actors = Stream.of(tagsInput.split(",")).filter(s -> !s.isBlank()).map(s -> {
                                    String[] split = s.split(":");
                                    Actor actor = new Actor();
                                    actor.setName(split[0]);
                                    actor.setRole(split[1]);
                                    return actor;
                                }).toList();
                                List<String> subtitles = Stream.of(tagsInput.split(",")).filter(s -> !s.isBlank()).toList();
                                Movie.MovieStatus status = Movie.MovieStatus.getStatusFromLevel(Integer.parseInt(statusInput));

                                if (status != null) {
                                    updateMovie.setTitle(title);
                                    updateMovie.setDescription(description);
                                    updateMovie.setDuration(duration);
                                    updateMovie.setImagePath(imagePath);
                                    updateMovie.setTags(tags);
                                    updateMovie.setDirectors(directors);
                                    updateMovie.setActors(actors);
                                    updateMovie.setRating(rating);
                                    updateMovie.setLanguage(language);
                                    updateMovie.setSubtitles(subtitles);
                                    updateMovie.setStatus(status);

                                    MovieDAO.createMovie(connection, updateMovie);
                                    response.sendRedirect("Movies");
                                    return;
                                } else {
                                    request.setAttribute("error", "Status is invalid.");
                                }
                            } else {
                                request.setAttribute("error", "All fields is required.");
                            }
                        } catch (CinemaException e) {
                            request.setAttribute("error", e.getMessage());
                        } catch (NumberFormatException e) {
                            request.setAttribute("error", "Duration or level is invalid.");
                        } catch (IndexOutOfBoundsException e) {
                            request.setAttribute("error", "Actors is invalid");
                        }
                        request.setAttribute("updateMovie", updateMovie);
                        forward(request, response);
                    } else {
                        printErrorRedirect(response.getWriter(), "Invalid Id.", "Movies");
                    }
                } else {
                    printErrorRedirect(response.getWriter(), "Invalid Id.", "Movies");
                }
            }
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), "Movies");
        } catch (NumberFormatException e) {
            printErrorRedirect(response.getWriter(), "Invalid id.", "Movies");
        }
    }

    @Override
    protected String getForwardPath() {
        return "/WEB-INF/MoviesUpdate.jsp";
    }
}

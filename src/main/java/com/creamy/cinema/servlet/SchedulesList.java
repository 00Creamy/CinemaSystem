package com.creamy.cinema.servlet;

import com.creamy.cinema.dao.ScheduleDAO;
import com.creamy.cinema.models.Schedule;
import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class SchedulesList extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = getUserFromSession(request, response);
            if (user != null) {
                if (verifyUser(request, user) && user.getAccessLevel().getLevel() >= User.AccessLevel.MANAGER.getLevel()) {
                    List<Schedule> schedules = ScheduleDAO.requestSchedules(connection);
                    request.setAttribute("schedules", schedules);
                } else {
                    List<Schedule> schedules = ScheduleDAO.requestSchedules(connection).stream().filter(Schedule::isActive).toList();
                    request.setAttribute("schedules", schedules);
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

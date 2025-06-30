package com.creamy.cinema.servlet;

import com.creamy.cinema.dao.HallDAO;
import com.creamy.cinema.dao.ScheduleDAO;
import com.creamy.cinema.models.Hall;
import com.creamy.cinema.models.Schedule;
import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SchedulesDelete extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response,2);
            if (user != null) {
                String idInput = request.getParameter("id");
                if (idInput != null) {
                    int id = Integer.parseInt(idInput);
                    Schedule deleteSchedule = ScheduleDAO.requestScheduleByScheduleId(connection, id);
                    if (deleteSchedule != null) {
                        deleteSchedule.setDeleted(true);
                        ScheduleDAO.updateSchedule(connection, deleteSchedule);
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
}

package com.creamy.cinema.servlet;

import com.creamy.cinema.dao.HallDAO;
import com.creamy.cinema.dao.UserDAO;
import com.creamy.cinema.models.Hall;
import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HallsDelete extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response,2);
            if (user != null) {
                String idInput = request.getParameter("id");
                if (idInput != null) {
                    int id = Integer.parseInt(idInput);
                    Hall deleteHall = HallDAO.requestHallByHallId(connection, id);
                    if (deleteHall != null) {
                        deleteHall.setDeleted(true);
                        HallDAO.updateHall(connection, deleteHall);
                    } else {
                        printErrorRedirect(response.getWriter(), "Invalid id.", "Halls");
                    }
                } else {
                    printErrorRedirect(response.getWriter(), "Invalid id.", "Halls");
                }
            }
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), "Halls");
        }
    }
}

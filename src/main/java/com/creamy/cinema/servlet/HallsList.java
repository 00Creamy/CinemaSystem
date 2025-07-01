package com.creamy.cinema.servlet;

import com.creamy.cinema.dao.HallDAO;
import com.creamy.cinema.models.Hall;
import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class HallsList extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response, 1);
            if (user != null) {
                String hallIdInput = request.getParameter("id");
                if (hallIdInput == null) {
                    List<Hall> halls = HallDAO.requestHalls(connection);
                    request.setAttribute("halls", halls);
                    forward(request, response);
                } else {
                    Hall hall = HallDAO.requestHallByHallId(connection, Integer.parseInt(hallIdInput));
                    if (hall != null) {
                        request.setAttribute("hall", hall);
                        forward(request, response, "/WEB-INF/HallsView.jsp");
                    } else {
                        printErrorRedirect(response.getWriter(), "Invalid id.", "Halls");
                    }
                }
            }
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), ".");
        } catch (NumberFormatException e) {
            printErrorRedirect(response.getWriter(), "Invalid id.", "Halls");
        }
    }

    @Override
    protected String getForwardPath() {
        return "/WEB-INF/Halls.jsp";
    }
}

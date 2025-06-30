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

public class HallsCreate extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response,2);
            if (user != null) {
                forward(request, response);
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
                String hallName = request.getParameter("hallName");
                String hallType = request.getParameter("hallType");
                String rowsInput = request.getParameter("rows");
                String seatPerRowInput = request.getParameter("seatPerRow");

                if (isStringValid(hallName, hallType, rowsInput, seatPerRowInput)) {
                    int rows = Integer.parseInt(rowsInput);
                    int seatPerRow = Integer.parseInt(seatPerRowInput);

                    Hall hall = new Hall();
                    hall.setHallName(hallName);
                    hall.setHallType(hallType);
                    hall.setRows(rows);
                    hall.setSeatPerRow(seatPerRow);
                    HallDAO.createHall(connection, hall);

                    response.sendRedirect("Halls");
                    return;
                } else {
                    request.setAttribute("error", "All fields is required.");
                }
                forward(request, response);
            }
        } catch (CinemaException e) {
            request.setAttribute("error", e.getMessage());
            forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Rows or Seat per row is invalid");
            forward(request, response);
        }
    }

    @Override
    protected String getForwardPath() {
        return "/WEB-INF/HallsAdd.jsp";
    }
}

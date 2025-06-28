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

public class HallsUpdate extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response,2);
            if (user != null) {
                String idInput = request.getParameter("id");
                if (idInput != null) {
                    int id = Integer.parseInt(idInput);
                    Hall updateHall = HallDAO.requestHallByHallId(connection, id);
                    if (updateHall != null) {
                        request.setAttribute("updateHall", updateHall);
                        forward(request, response);
                    } else {
                        printErrorRedirect(response.getWriter(), "Invalid id.", "Users");
                    }
                } else {
                    printErrorRedirect(response.getWriter(), "Invalid id.", "Users");
                }
            }
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), "Halls");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response,2);
            if (user != null) {
                String idInput = request.getParameter("id");
                String hallName = request.getParameter("hallName");
                String rowsInput = request.getParameter("rows");
                String seatPerRowInput = request.getParameter("seatPerRow");

                if (idInput != null) {
                    int id = Integer.parseInt(idInput);
                    Hall updateHall = HallDAO.requestHallByHallId(connection, id);

                    if (updateHall != null) {
                        try {
                            if (hallName != null && rowsInput != null && seatPerRowInput != null) {
                                int rows = Integer.parseInt(rowsInput);
                                int seatPerRow = Integer.parseInt(seatPerRowInput);

                                updateHall.setHallName(hallName);
                                updateHall.setRows(rows);
                                updateHall.setSeatPerRow(seatPerRow);
                                HallDAO.updateHall(connection, updateHall);

                                response.sendRedirect("Halls");
                                return;
                            } else {
                                request.setAttribute("error", "All fields is required.");
                            }
                        } catch (CinemaException e) {
                            request.setAttribute("error", e.getMessage());
                        } catch (NumberFormatException e) {
                            request.setAttribute("error", "Invalid rows or seat per row.");
                        }
                        request.setAttribute("updateHall", updateHall);
                        forward(request, response);
                    } else {
                        printErrorRedirect(response.getWriter(), "Invalid Id.", "Halls");
                    }
                } else {
                    printErrorRedirect(response.getWriter(), "Invalid Id.", "Halls");
                }
            }
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), "Halls");
        } catch (NumberFormatException e) {
            printErrorRedirect(response.getWriter(), "Invalid id.", "Halls");
        }
    }

    @Override
    protected String getForwardPath() {
        return "/WEB-INF/HallsUpdate.jsp";
    }
}

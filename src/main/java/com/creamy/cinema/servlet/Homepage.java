package com.creamy.cinema.servlet;

import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Homepage extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = getUserFromSession(request, response);
            if (user != null) {
                if (verifyUser(request, user) && user.getAccessLevel().getLevel() >= User.AccessLevel.STAFF.getLevel()) {
                    forward(request, response, "/WEB-INF/Dashboard.jsp");
                    return;
                }
            }
            forward(request, response);
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(),  e.getMessage(), ".");
        }
    }

    @Override
    protected String getForwardPath() {
        return "/WEB-INF/Homepage.jsp";
    }
}

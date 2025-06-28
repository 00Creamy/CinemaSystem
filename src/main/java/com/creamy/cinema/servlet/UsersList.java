package com.creamy.cinema.servlet;

import com.creamy.cinema.dao.UserDAO;
import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UsersList extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response, 1);
            if (user != null) {
                List<User> users = UserDAO.requestUsers(connection).stream().filter(user1 -> !user1.isDeleted()).toList();
                if (user.getAccessLevel().getLevel() < 2) {
                    users = users.stream().filter(user1 -> user1.getAccessLevel().getLevel() < 1).toList();
                }
                request.setAttribute("users", users);
                forward(request, response);
            }
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), ".");
        }
    }

    @Override
    protected String getForwardPath() {
        return "/WEB-INF/Users.jsp";
    }
}

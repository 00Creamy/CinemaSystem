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
                String userIdInput = request.getParameter("id");
                if (userIdInput ==  null) {
                    List<User> users = UserDAO.requestUsers(connection);
                    if (user.getAccessLevel().getLevel() < 2) {
                        users = users.stream().filter(user1 -> user1.getAccessLevel().getLevel() < 1).toList();
                    }
                    request.setAttribute("users", users);
                    forward(request, response);
                } else {
                    User userView = UserDAO.requestUserByUserId(connection, Integer.parseInt(userIdInput));
                    if (userView != null) {
                        request.setAttribute("user", userView);
                        forward(request, response, "/WEB-INF/UsersView.jsp");
                    } else {
                        printErrorRedirect(response.getWriter(), "Invalid id.", "Users");
                    }
                }
            }
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), ".");
        } catch (NumberFormatException e) {
            printErrorRedirect(response.getWriter(), "Invalid id.", "Users");
        }
    }

    @Override
    protected String getForwardPath() {
        return "/WEB-INF/Users.jsp";
    }
}

package com.creamy.cinema.servlet;

import com.creamy.cinema.dao.UserDAO;
import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsersCreate extends BaseServlet {
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
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String levelInput = request.getParameter("level");
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phoneNo = request.getParameter("phoneNo");

                if (username != null && password != null && levelInput != null && name != null && email != null && phoneNo != null) {
                    User.AccessLevel level = User.AccessLevel.getLevelByLevel(Integer.parseInt(levelInput));
                    // RegEx for checking phone no, Optional + at the front and decimal at the back
                    boolean validPhoneNo = phoneNo.matches("\\+?\\d+");
                    if (validPhoneNo && level != null && level.getLevel() < user.getAccessLevel().getLevel()) {
                        if (UserDAO.requestUserByUsername(connection, username) == null) {
                            User user1 = new User();
                            user1.setUsername(username);
                            user1.setPassword(password);
                            user1.setAccessLevel(level);
                            user1.setName(name);
                            user1.setEmail(email);
                            user1.setPhoneNo(phoneNo);
                            UserDAO.createUser(connection, user1);
                            response.sendRedirect("Users");
                            return;
                        } else {
                            request.setAttribute("error", "Username is taken.");
                        }
                    } else {
                        request.setAttribute("error", "Phone No or level is invalid.");
                    }
                } else {
                    request.setAttribute("error", "All fields is required.");
                }
                forward(request, response);
            }
        } catch (CinemaException e) {
            request.setAttribute("error", e.getMessage());
            forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Level is invalid");
            forward(request, response);
        }
    }

    @Override
    protected String getForwardPath() {
        return "/WEB-INF/UsersAdd.jsp";
    }
}

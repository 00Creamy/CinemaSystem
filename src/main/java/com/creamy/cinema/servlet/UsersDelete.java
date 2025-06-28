package com.creamy.cinema.servlet;

import com.creamy.cinema.dao.UserDAO;
import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsersDelete extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response,2);
            if (user != null) {
                String idInput = request.getParameter("id");
                if (idInput != null) {
                    int id = Integer.parseInt(idInput);
                    User deleteUser = UserDAO.requestUserByUserId(connection, id);
                    if (deleteUser != null) {
                        if (deleteUser.getAccessLevel().getLevel() < user.getAccessLevel().getLevel()) {
                            deleteUser.setDeleted(true);
                            UserDAO.updateUser(connection, deleteUser);
                            response.sendRedirect("Users");
                        } else {
                            printErrorRedirect(response.getWriter(), "Unauthorized access.", "Users");
                        }
                    } else {
                        printErrorRedirect(response.getWriter(), "Invalid id.", "Users");
                    }
                } else {
                    printErrorRedirect(response.getWriter(), "Invalid id.", "Users");
                }
            }
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), "Users");
        }
    }
}

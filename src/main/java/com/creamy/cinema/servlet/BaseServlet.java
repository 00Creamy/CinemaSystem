package com.creamy.cinema.servlet;

import com.creamy.cinema.dao.UserDAO;
import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;
import com.creamy.cinema.util.DBConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class BaseServlet extends HttpServlet {
    protected DBConnection connection;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            connection = new DBConnection();
        } catch (CinemaException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
    }

    @Override
    public void destroy() {
        super.destroy();
        connection.close();
    }

    public User authorizeUser(HttpServletRequest request, HttpServletResponse response) throws IOException, CinemaException {
        return authorizeUser(request, response, 0);
    }

    public User authorizeUser(HttpServletRequest request, HttpServletResponse response, int level) throws IOException, CinemaException {
        User user = getUserFromSession(request, response);
        if (user != null) {
            User fromDatabase = UserDAO.requestUserByUserId(connection, user.getUserId());
            if (fromDatabase.getSessionVersion() == user.getSessionVersion() && !fromDatabase.isDeleted()) {
                if (user.getAccessLevel().getLevel() >= level) {
                    return user;
                } else {
                    printErrorRedirect(response.getWriter(), "Unauthorized access.", ".");
                }
            } else {
                HttpSession session = request.getSession();
                session.invalidate();
                response.sendRedirect("Login");
            }
        } else {
            response.sendRedirect("Login");
        }
        return null;
    }

    public User getUserFromSession(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        return (User) session.getAttribute("user");
    }

    public static void printErrorRedirect(PrintWriter out, String message, String location) {
        out.print("<script>"
                + "alert(\"" + message + "\");"
                + "window.location.href='" + location + "';"
                + "</script>");
    }

    public void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(getForwardPath());
        dispatcher.forward(request, response);
    }

    protected String getForwardPath() {
        return ".";
    }
}

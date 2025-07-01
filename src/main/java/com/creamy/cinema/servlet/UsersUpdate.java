package com.creamy.cinema.servlet;

import com.creamy.cinema.dao.UserDAO;
import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsersUpdate extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response,2);
            if (user != null) {
                String idInput = request.getParameter("id");
                if (idInput != null) {
                    int id = Integer.parseInt(idInput);
                    User updateUser = UserDAO.requestUserByUserId(connection, id);
                    if (updateUser != null) {
                        if (updateUser.getUserId() == user.getUserId() || updateUser.getAccessLevel().getLevel() < user.getAccessLevel().getLevel()) {
                            request.setAttribute("updateUser", updateUser);
                            forward(request, response);
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
        } catch (NumberFormatException e) {
            printErrorRedirect(response.getWriter(), "Invalid id.", "Users");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response,2);
            if (user != null) {
                String idInput = request.getParameter("id");
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String levelInput = request.getParameter("level");
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phoneNo = request.getParameter("phoneNo");

                if (isStringValid(idInput)) {
                    int id = Integer.parseInt(idInput);
                    User updateUser = UserDAO.requestUserByUserId(connection, id);

                    if (updateUser != null) {
                        if (updateUser.getUserId() == user.getUserId() || updateUser.getAccessLevel().getLevel() < user.getAccessLevel().getLevel()) {
                            try {
                                if (isStringValid(username, name, email, phoneNo)) {
                                    // RegEx for checking phone no, Optional + at the front and decimal at the back
                                    boolean validPhoneNo = phoneNo.matches("\\+?\\d+");

                                    if (validPhoneNo) {
                                        User usernameCheck = UserDAO.requestUserByUsername(connection, username);
                                        if (usernameCheck == null || usernameCheck.getUserId() == updateUser.getUserId()) {
                                            if (user.getUserId() != updateUser.getUserId()) {
                                                if (isStringValid(levelInput)) {
                                                    User.AccessLevel level = User.AccessLevel.getLevelByLevel(Integer.parseInt(levelInput));
                                                    if (level != null && level.getLevel() < user.getAccessLevel().getLevel()) {
                                                        updateUser.setAccessLevel(level);
                                                    } else {
                                                        request.setAttribute("error", "Invalid level.");
                                                        request.setAttribute("updateUser", updateUser);
                                                        forward(request, response);
                                                        return;
                                                    }
                                                } else {
                                                    request.setAttribute("error", "Invalid level.");
                                                    request.setAttribute("updateUser", updateUser);
                                                    forward(request, response);
                                                    return;
                                                }
                                            }

                                            if (isStringValid(password)) {
                                                updateUser.setPassword(password);
                                                UserDAO.updateUserPassword(connection, updateUser);
                                            } else {
                                                request.setAttribute("error", "Invalid password.");
                                                request.setAttribute("updateUser", updateUser);
                                                forward(request, response);
                                                return;
                                            }

                                            updateUser.setUsername(username);
                                            updateUser.setName(name);
                                            updateUser.setEmail(email);
                                            updateUser.setPhoneNo(phoneNo);
                                            UserDAO.updateUser(connection, updateUser);

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
                            } catch (CinemaException e) {
                                request.setAttribute("error", e.getMessage());
                            } catch (NumberFormatException e) {
                                request.setAttribute("error", "Invalid level.");
                            }
                            request.setAttribute("updateUser", updateUser);
                            forward(request, response);
                        } else {
                            printErrorRedirect(response.getWriter(), "Unauthorized access.", "Users");
                        }
                    } else {
                        printErrorRedirect(response.getWriter(), "Invalid Id.", "Users");
                    }
                } else {
                    printErrorRedirect(response.getWriter(), "Invalid Id.", "Users");
                }
            }
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), "Users");
        } catch (NumberFormatException e) {
            printErrorRedirect(response.getWriter(), "Invalid id.", "Users");
        }
    }

    @Override
    protected String getForwardPath() {
        return "/WEB-INF/UsersUpdate.jsp";
    }
}

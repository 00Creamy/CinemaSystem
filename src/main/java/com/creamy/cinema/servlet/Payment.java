package com.creamy.cinema.servlet;

import com.creamy.cinema.dao.BookingDAO;
import com.creamy.cinema.dao.HallDAO;
import com.creamy.cinema.models.Booking;
import com.creamy.cinema.models.Hall;
import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Payment extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response,1);
            if (user != null) {
                forward(request, response);
            }
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), ".");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            User user = authorizeUser(request, response,1);
            if (user != null) {
                String idInput = request.getParameter("id");

                if (isStringValid(idInput)) {
                    int id = Integer.parseInt(idInput);

                    Booking booking = BookingDAO.requestBookingByBookingId(connection, id);
                    booking.setStatus(Booking.BookingStatus.PAYED);
                    BookingDAO.updateBooking(connection, booking);

                    response.sendRedirect("Bookings");
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
            request.setAttribute("error", "Id is invalid");
            forward(request, response);
        }
    }

    @Override
    protected String getForwardPath() {
        return "/WEB-INF/Payment.jsp";
    }
}

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
import java.util.List;

public class BookingsCreate extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response,1);
            String scheduleIdInput = request.getParameter("scheduleId");
            if (user != null) {
                if (isStringValid(scheduleIdInput)) {
                    int scheduleId = Integer.parseInt(scheduleIdInput);

                    List<String> seats = BookingDAO.requestAvailableSeats(connection, scheduleId);
                    request.setAttribute("seats", seats);

                    forward(request, response);
                } else {
                    printErrorRedirect(response.getWriter(), "Invalid id", "Schedules");
                }
            }
        } catch (CinemaException e) {
            printErrorRedirect(response.getWriter(), e.getMessage(), "Schedules");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = authorizeUser(request, response,2);
            if (user != null) {
                String scheduleIdInput = request.getParameter("scheduleId");
                String seat = request.getParameter("seat");

                if (isStringValid(scheduleIdInput, seat)) {
                    int scheduleId = Integer.parseInt(scheduleIdInput);

                    Booking booking = new Booking();
                    booking.setScheduleId(scheduleId);
                    booking.setSeat(seat);
                    booking.setStatus(Booking.BookingStatus.RESERVED);
                    BookingDAO.createBooking(connection, booking);

                    response.sendRedirect("Payment?id=" + booking.getBookingId());
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
            request.setAttribute("error", "Schedule Id is invalid");
            forward(request, response);
        }
    }

    @Override
    protected String getForwardPath() {
        return "/WEB-INF/BookingsAdd.jsp";
    }
}

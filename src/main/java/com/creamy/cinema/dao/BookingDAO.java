package com.creamy.cinema.dao;

import com.creamy.cinema.models.Booking;
import com.creamy.cinema.models.Hall;
import com.creamy.cinema.models.Schedule;
import com.creamy.cinema.util.CinemaException;
import com.creamy.cinema.util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    public static void createBooking(DBConnection connection, Booking booking) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareInsertStatement("INSERT INTO booking (scheduleId, seat, status) VALUES (?, ?, ?)");

            statement.setInt(1, booking.getScheduleId());
            statement.setString(2, booking.getSeat());
            statement.setInt(3, booking.getStatus().getNumber());

            int bookingId = connection.executeInsertStatement(statement);
            booking.setBookingId(bookingId);
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to create booking.", e);
        }
    }

    public static List<Booking> requestBookings(DBConnection connection) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM booking");
            ArrayList<Booking> bookings = new ArrayList<>();
            ResultSet resultSet = connection.executeStatement(statement);
            while (resultSet.next()) {
                bookings.add(getBookingFromResultSet(resultSet));
            }
            return bookings;
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to request booking.", e);
        }
    }

    public static Booking requestBookingByBookingId(DBConnection connection, int bookingId) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM booking WHERE booking_id=?");
            statement.setInt(1, bookingId);

            ResultSet resultSet = connection.executeStatement(statement);
            if (resultSet.next()) {
                return getBookingFromResultSet(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to request hall.", e);
        }
    }

    public static List<Booking> requestBookingByScheduleId(DBConnection connection, int scheduleId) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM booking WHERE schedule_id=?");
            statement.setInt(1, scheduleId);
            ArrayList<Booking> bookings = new ArrayList<>();
            ResultSet resultSet = connection.executeStatement(statement);
            while (resultSet.next()) {
                bookings.add(getBookingFromResultSet(resultSet));
            }
            return bookings;
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to request hall.", e);
        }
    }

    public static List<String> requestAvailableSeats(DBConnection connection, int scheduleId) throws CinemaException {
        Schedule schedule = ScheduleDAO.requestScheduleByScheduleId(connection, scheduleId);
        List<String> seats = HallDAO.requestAllSeat(connection, schedule.getHallId());
        seats.removeAll(requestBookingByScheduleId(connection, scheduleId).stream().map(Booking::getSeat).toList());
        return seats;
    }

    public static boolean updateBooking(DBConnection connection, Booking booking) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE booking SET schedule_id=?, seat=?, status=? WHERE booking_id=?");
            statement.setInt(1, booking.getScheduleId());
            statement.setString(2, booking.getSeat());
            statement.setInt(3, booking.getStatus().getNumber());
            statement.setInt(4, booking.getBookingId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to update booking.", e);
        }
    }

    public static boolean deleteBooking(DBConnection connection, int bookingId) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM booking WHERE booking_id=?");
            statement.setInt(1, bookingId);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to delete booking.", e);
        }
    }

    private static Booking getBookingFromResultSet(ResultSet resultSet) throws SQLException {
        Booking hall = new Booking();
        hall.setBookingId(resultSet.getInt("hall_id"));
        hall.setScheduleId(resultSet.getInt("name"));
        hall.setSeat(resultSet.getString("type"));
        hall.setAddedDatetime(resultSet.getTimestamp("hall_rows").toLocalDateTime());
        hall.setStatus(Booking.BookingStatus.getBookingStatusByNumber(resultSet.getInt("seat_per_row")));
        return hall;
    }
}

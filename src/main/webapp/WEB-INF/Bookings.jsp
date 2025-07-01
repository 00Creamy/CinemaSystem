<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="com.creamy.cinema.models.User" %>
<%@ page import="com.creamy.cinema.models.Booking" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link href="default.css" rel="stylesheet">
</head>
<body>
    <%
    User user = (User) session.getAttribute("user");
    List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
    %>
    <jsp:include page="Sidebar.jsp" />
    <div id="content">
        <table border="1">
            <thead>
                <tr>
                    <td>Schedule Id</td>
                    <td>Seat</td>
                    <td>Status</td>
                    <%=(user.getAccessLevel().getLevel() >= 2) ? "<td colspan='1'>Action</td>" : ""%>
                </tr>
            </thead>
            <tbody>
                <%
                for (Booking booking: bookings) {
                %>
                <tr>
                    <td><%=booking.getScheduleId()%></td>
                    <td><%=booking.getSeat()%></td>
                    <td><%=booking.getStatus().getName()%></td>
                    <%=(user.getAccessLevel().getLevel() >= 1 && booking.getStatus() == Booking.BookingStatus.RESERVED) ? "<td><a href='./Payment?id=" + booking.getBookingId() + "'>Payment</a></td>" : ""%>
                </tr>
                <%
                }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>
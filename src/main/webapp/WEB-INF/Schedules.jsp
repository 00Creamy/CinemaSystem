<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="com.creamy.cinema.models.User" %>
<%@ page import="com.creamy.cinema.models.Hall" %>
<%@ page import="com.creamy.cinema.models.Movie" %>
<%@ page import="com.creamy.cinema.models.Schedule" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
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
    List<Schedule> schedules = (List<Schedule>) request.getAttribute("schedules");
    Map<Integer, Movie> movies = (Map<Integer, Movie>) request.getAttribute("movies");
    Map<Integer, Hall> halls = (Map<Integer, Hall>) request.getAttribute("halls");
    %>
    <jsp:include page="Sidebar.jsp" />
    <div id="content">
        <%=(user.getAccessLevel().getLevel() >= 2) ? "<a href='AddSchedule'>Add Schedule</a>" : ""%>
        <table border="1">
            <thead>
                <tr>
                    <td>Movie</td>
                    <td>Hall</td>
                    <td>Start Time</td>
                    <td>End Time</td>
                    <%=(user.getAccessLevel().getLevel() >= 1) ? "<td colspan='3'>Action</td>" : ""%>
                </tr>
            </thead>
            <tbody>
                <%
                for (Schedule schedule: schedules) {
                %>
                <tr>
                    <td><%=movies.get(schedule.getMovieId()).getTitle()%></td>
                    <td><%=halls.get(schedule.getHallId()).getHallName()%></td>
                    <td><%=schedule.getStartDatetime().toString()%></td>
                    <td><%=schedule.getEndDatetime().toString()%></td>
                    <%=(user.getAccessLevel().getLevel() >= 2) ? "<td><a href='./AddBooking?id=" + schedule.getScheduleId() + "'>Add Booking</a></td>" : ""%>
                    <%=(user.getAccessLevel().getLevel() >= 2) ? "<td><a href='./UpdateSchedule?id=" + schedule.getScheduleId() + "'>Update</a></td>" : ""%>
                    <%=(user.getAccessLevel().getLevel() >= 2) ? "<td><a href='./DeleteSchedule?id=" + schedule.getScheduleId() + "'>Delete</a></td>" : ""%>
                </tr>
                <%
                }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>
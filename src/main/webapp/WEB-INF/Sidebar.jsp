<%@ page import="com.creamy.cinema.models.User" %>
<%
User user = (User) session.getAttribute("user");
%>
<div id="sidebar">
    <ul>
        <li><a href="./Users">Users</a></li>
        <li><a href="./Halls">Halls</a></li>
        <li><a href="./Movies">Movies</a></li>
        <li><a href="./Schedules">Schedules</a></li>
        <li><a href="./Bookings">Bookings</a></li>
    </ul>
</div>
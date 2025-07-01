<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="com.creamy.cinema.models.User" %>
<%@ page import="com.creamy.cinema.models.Hall" %>
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
    List<Hall> halls = (List<Hall>) request.getAttribute("halls");
    %>
    <jsp:include page="Sidebar.jsp" />
    <div id="content">
        <%=(user.getAccessLevel().getLevel() >= 2) ? "<a href='AddHall'>Add Hall</a>" : ""%>
        <table border="1">
            <thead>
                <tr>
                    <td>Hall Name</td>
                    <td>Hall Type</td>
                    <td>Rows</td>
                    <td>Seat per row</td>
                    <%=(user.getAccessLevel().getLevel() >= 2) ? "<td colspan='2'>Action</td>" : ""%>
                </tr>
            </thead>
            <tbody>
                <%
                for (Hall hall: halls) {
                %>
                <tr>
                    <td><%=hall.getHallName()%></td>
                    <td><%=hall.getHallType()%></td>
                    <td><%=hall.getRows()%></td>
                    <td><%=hall.getSeatPerRow()%></td>
                    <%=(user.getAccessLevel().getLevel() >= 2) ? "<td><a href='./UpdateHall?id=" + hall.getHallId() + "'>Update</a></td>" : ""%>
                    <%=(user.getAccessLevel().getLevel() >= 2) ? "<td><a href='./DeleteHall?id=" + hall.getHallId() + "'>Delete</a></td>" : ""%>
                </tr>
                <%
                }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>
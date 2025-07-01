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
    Hall hall = (Hall) request.getAttribute("hall");
    %>
    <jsp:include page="Sidebar.jsp" />
    <div id="content">
        <table border="1">
            <tbody>
                <tr>
                    <td>Hall Name: </td>
                    <td><%=hall.getHallName()%></td>
                </tr>
                <tr>
                    <td>Hall Type: </td>
                    <td><%=hall.getHallType()%></td>
                </tr>
                <tr>
                    <td>Rows: </td>
                    <td><%=hall.getRows()%></td>
                </tr>
                <tr>
                    <td>Seat Per Row: </td>
                    <td><%=hall.getSeatPerRow()%></td>
                </tr>
            </tbody>
        </table>
        <%
        if (user.getAccessLevel().getLevel() >= 2) {
        %>
        <a href='./UpdateHall?id=<%=hall.getHallId()%>'>Update</a>
        <%
        }
        if (user.getAccessLevel().getLevel() >= 2) {
        %>
        <a href='./DeleteHall?id=<%=hall.getHallId()%>'>Delete</a>
        <%
        }
        %>
    </div>
</body>
</html>
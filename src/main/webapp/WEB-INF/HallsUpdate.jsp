<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="com.creamy.cinema.models.User"%>
<%@ page import="com.creamy.cinema.models.Hall"%>
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
    Hall updateHall = (Hall) request.getAttribute("updateHall");
    %>
    <jsp:include page="Sidebar.jsp" />
    <div id="content">
        <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
            out.println("<p>" + error + "</p>");
        }
        %>
        <form method="post">
            <input type="hidden" name="id" value="<%=updateHall.getHallId()%>">
            Hall Name: <input type="text" name="hallName" value="<%=updateHall.getHallName()%>" required><br>
            Hall Type: <input type="text" name="hallType" value="<%=updateHall.getHallType()%>" required><br>
            Rows: <input type="number" name="rows" value="<%=updateHall.getRows()%>" required><br>
            Seat Per Row: <input type="number" name="seatPerRow" value="<%=updateHall.getSeatPerRow()%>" required><br>
            <input type="submit" value="Submit">
        </form>
    </div>
</body>
</html>
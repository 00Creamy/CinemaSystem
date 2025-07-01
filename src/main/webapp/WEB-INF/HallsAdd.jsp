<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="com.creamy.cinema.models.User"%>
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
            Hall Name: <input type="text" name="hallName" required><br>
            Hall Type: <input type="text" name="hallType" required><br>
            Rows: <input type="number" name="rows" required><br>
            Seat Per Row: <input type="number" name="seatPerRow" required><br>
            <input type="submit" value="Submit">
        </form>
    </div>
</body>
</html>
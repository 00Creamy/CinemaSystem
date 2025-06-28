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
            Username: <input type="text" name="username" required><br>
            Password: <input type="password" name="password" required><br>
            Access Level:
            <select name="level" required>
                <option value="" disabled selected>Choose Access Level</option>
                <option value="0">Customer</option>
                <option value="1">Staff</option>
                <%=(user.getAccessLevel().getLevel() >= 3) ? "<option value='2'>Manager</option>" : ""%>
            </select><br>
            Name: <input type="text" name="name" required><br>
            Email: <input type="text" name="email" required><br>
            Phone No: <input type="text" name="phoneNo" required><br>
            <input type="submit" value="Submit">
        </form>
    </div>
</body>
</html>
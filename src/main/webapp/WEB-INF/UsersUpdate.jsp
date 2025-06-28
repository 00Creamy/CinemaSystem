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
    User updateUser = (User) request.getAttribute("updateUser");
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
            <input type="hidden" name="id" value="<%=updateUser.getUserId()%>">
            Username: <input type="text" name="username" value="<%=updateUser.getUsername()%>" required><br>
            Password: <input type="password" name="password"><br>
            <%
            if (updateUser.getUserId() != user.getUserId()) {
            %>
            Access Level:
            <select name="level" required>
                <option value="" disabled selected>Choose Access Level</option>
                <option value="0" <%=(updateUser.getAccessLevel().getLevel() == 0) ? "selected" : ""%>>Customer</option>
                <option value="1" <%=(updateUser.getAccessLevel().getLevel() == 1) ? "selected" : ""%>>Staff</option>
                <%=(user.getAccessLevel().getLevel() >= 3) ? "<option value='2'" + ((updateUser.getAccessLevel().getLevel() == 2) ? "selected" : "") + ">Manager</option>" : ""%>
            </select><br>
            <%
            }
            %>
            Name: <input type="text" name="name" value="<%=updateUser.getName()%>" required><br>
            Email: <input type="text" name="email" value="<%=updateUser.getEmail()%>" required><br>
            Phone No: <input type="text" name="phoneNo" value="<%=updateUser.getPhoneNo()%>" required><br>
            <input type="submit" value="Submit">
        </form>
    </div>
</body>
</html>
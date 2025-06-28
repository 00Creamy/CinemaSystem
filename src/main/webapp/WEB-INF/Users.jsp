<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="com.creamy.cinema.models.User" %>
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
    List<User> users = (List<User>) request.getAttribute("users");
    %>
    <jsp:include page="Sidebar.jsp" />
    <div id="content">
        <%=(user.getAccessLevel().getLevel() >= 2) ? "<a href='AddUser'>Add User</a>" : ""%>
        <table border="1">
            <thead>
                <tr>
                    <td>Username</td>
                    <td>Name</td>
                    <td>Email</td>
                    <td>Phone No</td>
                    <%=(user.getAccessLevel().getLevel() >= 2) ? "<td>Access Level</td>" : ""%>
                    <%=(user.getAccessLevel().getLevel() >= 2) ? "<td colspan='2'>Action</td>" : ""%>
                </tr>
            </thead>
            <tbody>
                <%
                for (User user1: users) {
                %>
                <tr>
                    <td><%=user1.getUsername()%></td>
                    <td><%=user1.getName()%></td>
                    <td><%=user1.getEmail()%></td>
                    <td><%=user1.getPhoneNo()%></td>
                    <%=(user.getAccessLevel().getLevel() >= 2) ? "<td>" + user1.getAccessLevel().getName() + "</td>" : ""%>
                    <%=(user.getAccessLevel().getLevel() >= 2 && (user.getAccessLevel().getLevel() > user1.getAccessLevel().getLevel() || user.getUserId() == user1.getUserId()) ? "<td><a href='./UpdateUser?id=" + user1.getUserId() + "'>Update</a></td>" : ""%>
                    <%=(user.getAccessLevel().getLevel() >= 2 && user.getAccessLevel().getLevel() > user1.getAccessLevel().getLevel()) ? "<td><a href='./DeleteUser?id=" + user1.getUserId() + "'>Delete</a></td>" : ""%>
                </tr>
                <%
                }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>
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
    User userView = (User) request.getAttribute("user");
    %>
    <jsp:include page="Sidebar.jsp" />
    <div id="content">
        <table border="1">
            <tbody>
                <tr>
                    <td>Username: </td>
                    <td><%=userView.getUsername()%></td>
                </tr>
                <tr>
                    <td>Name: </td>
                    <td><%=userView.getName()%></td>
                </tr>
                <tr>
                    <td>Email: </td>
                    <td><%=userView.getEmail()%></td>
                </tr>
                <tr>
                    <td>Phone No: </td>
                    <td><%=userView.getPhoneNo()%></td>
                </tr>
                <%
                if (user.getAccessLevel().getLevel() >= 2) {
                %>
                <tr>
                    <td>Access Level: </td>
                    <td><%=userView.getAccessLevel().getName()%></td>
                </tr>
                <%
                }
                %>
            </tbody>
        </table>
        <%
        if (user.getAccessLevel().getLevel() >= 2 && (user.getAccessLevel().getLevel() > userView.getAccessLevel().getLevel() || user.getUserId() == userView.getUserId())) {
        %>
        <a href='./UpdateUser?id=<%=userView.getUserId()%>'>Update</a>
        <%
        }
        if (user.getAccessLevel().getLevel() >= 2 && user.getAccessLevel().getLevel() > userView.getAccessLevel().getLevel()) {
        %>
        <a href='./DeleteUser?id=<%=userView.getUserId()%>'>Delete</a>
        <%
        }
        %>
    </div>
</body>
</html>
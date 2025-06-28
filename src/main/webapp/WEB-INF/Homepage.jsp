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
        <h1>Welcome, <%=user.getName()%></h1><br>
    </div>
</body>
</html>
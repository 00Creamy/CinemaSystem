<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link href="loginstyle.css" rel="stylesheet">
</head>
<body>
    <div class="left">
        <div>HELLO!!!</div>
    </div>
    <div class="right">
        <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
        %>
            <p class="error"><%= error %></p>
        <%
        }
        %>
        <form method="post">
            <label>Username:</label>
            <input type="text" name="username"><br>
            <label>Password:</label>
            <input type="password" name="password"><br>
            <input type="submit" value="Login">
        </form>
    </div>
</body>
</html>
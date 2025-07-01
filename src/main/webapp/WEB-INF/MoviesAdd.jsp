<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="com.creamy.cinema.models.User"%>
<%@ page import="com.creamy.cinema.models.Movie"%>
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
            Title: <input type="text" name="title" required><br>
            Description: <input type="text" name="description" required><br>
            Duration: <input type="number" name="hours" required>:<input type="number" name="mins" required>:<input type="number" name="seconds" required><br>
            Tags: <input type="text" name="tags" required><br>
            Directors: <input type="text" name="directors" required><br>
            Actors: <input type="text" name="actors" required><br>
            Rating: <input type="text" name="rating" required><br
            Language: <input type="text" name="language" required><br>
            Subtitles: <input type="text" name="subtitles" required><br>
            Status: <select name="status">
                <%
                for (Movie.MovieStatus status: Movie.MovieStatus.values()) {
                %>
                <option value="<%=status.getLevel()%>"><%=status.getName()%></option>
                <%
                }
                %>
            </select><br>
            <input type="submit" value="Submit">
        </form>
    </div>
</body>
</html>
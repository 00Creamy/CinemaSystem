<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="com.creamy.cinema.models.User"%>
<%@ page import="com.creamy.cinema.models.Movie"%>
<%@ page import="com.creamy.cinema.models.Hall"%>
<%@ page import="java.util.List"%>
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
    List<Movie> movies = (List<Movie>) request.getAttribute("movies");
    List<Hall> halls = (List<Hall>) request.getAttribute("halls");
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
            <select name="movieId">
                <%
                for (Movie movie: movies) {
                %>
                <option value="<%=movie.getMovieId()%>"><%=movie.getTitle()%></option>
                <%
                }
                %>
            </select>
            <select name="hallId">
                <%
                for (Hall hall: halls) {
                %>
                <option value="<%=hall.getHallId()%>"><%=hall.getHallName()%></option>
                <%
                }
                %>
            </select>
            Start Time: <input type="datetime-local" name="startDatetime" required><br>
            End Time: <input type="datetime-local" name="endDatetime" required><br>
            <input type="submit" value="Submit">
        </form>
    </div>
</body>
</html>
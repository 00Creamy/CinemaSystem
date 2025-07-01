<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="com.creamy.cinema.models.User" %>
<%@ page import="com.creamy.cinema.models.Movie" %>
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
    List<Movie> movies = (List<Movie>) request.getAttribute("movies");
    %>
    <jsp:include page="Sidebar.jsp" />
    <div id="content">
        <%=(user.getAccessLevel().getLevel() >= 2) ? "<a href='AddMovie'>Add Movie</a>" : ""%>
        <table border="1">
            <thead>
                <tr>
                    <td>Title</td>
                    <td>Duration</td>
                    <td>Rating</td>
                    <td>Language</td>
                    <td>Status</td>
                    <td colspan="3">Action</td>
                </tr>
            </thead>
            <tbody>
                <%
                for (Movie movie: movies) {
                %>
                <tr>
                    <td><%=movie.getTitle()%></td>
                    <td><%=String.format("%02d:%02d:%02d", movie.getDuration().toHours(), movie.getDuration().toMinutesPart(), movie.getDuration().toSecondsPart())%></td>
                    <td><%=movie.getRating()%></td>
                    <td><%=movie.getLanguage()%></td>
                    <td><%=movie.getStatus().getName()%></td>
                    <td><a href="./Movies?id=<%=movie.getMovieId()%>">View</a></td>
                    <%=(user.getAccessLevel().getLevel() >= 2) ? "<td><a href='./UpdateMovie?id=" + movie.getMovieId() + "'>Update</a></td>" : ""%>
                    <%=(user.getAccessLevel().getLevel() >= 2) ? "<td><a href='./DeleteMovie?id=" + movie.getMovieId() + "'>Delete</a></td>" : ""%>
                </tr>
                <%
                }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>
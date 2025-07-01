<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="com.creamy.cinema.models.User" %>
<%@ page import="com.creamy.cinema.models.Movie" %>
<%@ page import="com.creamy.cinema.models.Actor" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
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
    Movie movie = (Movie) request.getAttribute("movie");
    List<String> actors = new ArrayList();
    for (Actor actor: movie.getActors()) {
        actors.add(actor.getName() + ":" + actor.getRole());
    }
    %>
    <jsp:include page="Sidebar.jsp" />
    <div id="content">
        <table border="1">
            <tbody>
                <tr>
                    <td>Title: </td>
                    <td><%=movie.getTitle()%></td>
                </tr>
                <tr>
                    <td>Description: </td>
                    <td><%=movie.getDescription()%></td>
                </tr>
                <tr>
                    <td>Duration: </td>
                    <td><%=String.format("%02d:%02d:%02d", movie.getDuration().toHours(), movie.getDuration().toMinutesPart(), movie.getDuration().toSecondsPart())%></td>
                </tr>
                <tr>
                    <td>Tags: </td>
                    <td><%=String.join(", ", movie.getTags())%></td>
                </tr>
                <tr>
                    <td>Directors: </td>
                    <td><%=String.join(", ", movie.getDirectors())%></td>
                </tr>
                <tr>
                    <td>Actors: </td>
                    <td><%=String.join(", ", actors)%></td>
                </tr>
                <tr>
                    <td>Rating: </td>
                    <td><%=movie.getRating()%></td>
                </tr>
                <tr>
                    <td>Language: </td>
                    <td><%=movie.getLanguage()%></td>
                </tr>
                <tr>
                    <td>Subtitles: </td>
                    <td><%=String.join(", ", movie.getSubtitles())%></td>
                </tr>
                <tr>
                    <td>Status: </td>
                    <td><%=movie.getStatus().getName()%></td>
                </tr>
            </tbody>
        </table>
        <%
        if (user.getAccessLevel().getLevel() >= 2) {
        %>
        <a href='./UpdateMovie?id=<%=movie.getMovieId()%>'>Update</a>
        <%
        }
        if (user.getAccessLevel().getLevel() >= 2) {
        %>
        <a href='./DeleteMovie?id=<%=movie.getMovieId()%>'>Delete</a>
        <%
        }
        %>
    </div>
</body>
</html>
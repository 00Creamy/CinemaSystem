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
    Movie updateMovie = (Movie) request.getAttribute("updateMovie");
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
            <input type="hidden" name="id" value="<%=updateMovie.getMovieId()%>"><br>
            Title: <input type="text" name="title" value="<%=updateMovie.getTitle()%>" required><br>
            Description: <input type="text" name="description" value="<%=updateMovie.getDescription()%>" required><br>
            Duration: <input type="number" name="hours" value="<%=updateMovie.getDuration().toHours()%>" required>:<input type="number" name="mins" value="<%=updateMovie.getDuration().toMinutesPart()%>" required>:<input type="number" name="seconds" value="<%=updateMovie.getDuration().toSecondsPart()%>" required><br>
            Tags: <input type="text" name="tags" value="<%=String.join(",", updateMovie.getTags())%>" required><br>
            Directors: <input type="text" name="directors" value="<%=String.join(",", updateMovie.getDirectors())%>" required><br>
            Actors: <input type="text" name="actors" value="<%=String.join(",", updateMovie.getActors().stream().map(actor -> actor.getName() + ": " + actor.getRole()).toList())%>" required><br>
            Rating: <input type="text" name="rating" value="<%=updateMovie.getRating()%>" required><br
            Language: <input type="text" name="language" value="<%=updateMovie.getLanguage()%>" required><br>
            Subtitles: <input type="text" name="subtitles" value="<%=String.join(",", updateMovie.getSubtitles())%>" required><br>
            Status: <select name="status">
                <%
                for (Movie.MovieStatus status: Movie.MovieStatus.values()) {
                %>
                <option value="<%=status.getLevel()%>" <%=(status == updateMovie.getStatus()) ? "selected" : ""%>><%=status.getName()%></option>
                <%
                }
                %>
            </select><br>
            <input type="submit" value="Submit">
        </form>
    </div>
</body>
</html>
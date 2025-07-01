<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="com.creamy.cinema.models.User"%>
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
    List<String> seats = (List<String>) request.getAttribute("seats");
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
            <input type="hidden" name="scheduleId" value="<%=request.getParameter("scheduleId")"%>">
            Seat:
            <select name="seat">
                <%
                for (String seat: seats) {
                %>
                <option value="<%=seat%>"><%=seat%></option>
                <%
                }
                %>
            </select>
            <br>

            <input type="submit" value="Submit">
        </form>
    </div>
</body>
</html>
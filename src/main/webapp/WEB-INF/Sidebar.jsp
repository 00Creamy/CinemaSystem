<%@ page import="com.creamy.cinema.models.User" %>
<%
User user = (User) session.getAttribute("user");
%>
<div id="sidebar">
    <ul>
        <li><a href="./Users">Users</a></li>
    </ul>
</div>
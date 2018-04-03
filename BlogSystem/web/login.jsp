<%-- 
    Document   : login
    Created on : 21-Nov-2017, 13:18:35
    Author     : d00186050
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <% 
    session.removeAttribute("login_status");
    session.removeAttribute("login_message");
    %>
    
    <body>
        <form action="FrontController" method="post">
            <h2>Username:</h2>
            <br>
            <input type="text" name="username" required>
            <br>
            <h2>Password:</h2>
            <br>
            <input type="password" name="password" required>
            <br>
            <input type="submit" value="Login" />
            <input type="hidden" name="action" value="login" />
        </form>
        <a href="index.html">Home</a>
    </body>
</html>

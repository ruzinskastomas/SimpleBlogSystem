<%-- 
    Document   : error
    Created on : 21-Nov-2017, 13:51:24
    Author     : d00186050
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <a href="login.jsp">Home</a>
    <body>
        <% Object msg = session.getAttribute("errorMessage");
        
        String message = (String) msg;%>
        <%=message%>
    </body>
</html>

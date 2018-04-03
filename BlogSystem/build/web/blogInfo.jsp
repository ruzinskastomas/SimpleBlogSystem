<%-- 
    Document   : blogInfo
    Created on : 21-Nov-2017, 14:30:04
    Author     : d00186050
--%>

<%@page import="Business.BlogEntry"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ArrayList"%>
<%@page import="DAO.BlogEntryDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <a href="login.jsp">Logout</a>
        <a href="myBlog.jsp">Home</a>
        <a href="viewFriends.jsp">Friends List</a>
        <%
            BlogEntryDao blogentrydao = new BlogEntryDao("myBlog");
            String friend = request.getParameter("friendname");
            ArrayList<BlogEntry> blogs = new ArrayList();
            blogs = blogentrydao.findBlogEntriesByAuthor(friend);
            String message = "";
            int arraylistsize = blogs.size();%>
        <% if (arraylistsize <= 0) {
                message = "No blog entries have been made!";%>
        <%=message%> <% } else {%><table>
            <tr>
                <th>Blog Title:</th>
                <th>Content:</th>
            </tr>

            <%for (int i = 0; i < arraylistsize; i++) {
            if(i >10) {
            arraylistsize = 1;
            }
            else {
            %>
            <tr>
                <td><%=blogs.get(i).getTitle()%></td>
                <td><%=blogs.get(i).getContent()%></td>
            </tr>
            <%  }
                }
            }
            %>


            %>
    </body>
</html>

<%-- 
    Document   : myBlog
    Created on : 21-Nov-2017, 13:42:20
    Author     : d00186050
--%>

<%@page import="Business.Friendship"%>
<%@page import="DAO.FriendshipDao"%>
<%@page import="Business.BlogEntry"%>
<%@page import="java.util.ArrayList"%>
<%@page import="DAO.BlogEntryDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Logged in</title>
    </head>
    <a href="login.jsp">Logout</a>
    <a href="viewFriends.jsp">View all friends online</a>
    <body>
        <h2>Logged in!</h2>
        <% Object msg = session.getAttribute("login_message");
            String username = (String) msg;
            String message = "Hello: " + username;
            BlogEntryDao blogentrydao = new BlogEntryDao("myBlog");
            ArrayList<BlogEntry> blogs = new ArrayList();
            blogs = blogentrydao.findBlogEntriesByAuthor(username);
            String message2 = "";
            int arraylistsize = blogs.size();%>
        <%=message%>
        <% if (arraylistsize <= 0) {
                message2 = "No blog entries have been made!";%>
        <%=message2%><% } else { %>
        <table>
            <tr>
                <th>Blog Title:</th>
                <th>Content:</th>
            </tr>

            <%for (int i = 0; i < arraylistsize; i++) {
                    if (i > 10) {
                        arraylistsize = 1;
                    } else {
            %>
            <tr>
                <td><%=blogs.get(i).getTitle()%></td>
                <td><%=blogs.get(i).getContent()%></td>
            </tr>
            <%  }
                    }
                }
            %>
        </table>
    </body>
</html>

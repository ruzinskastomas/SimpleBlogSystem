<%-- 
    Document   : viewFriends
    Created on : 21-Nov-2017, 14:14:37
    Author     : d00186050
--%>

<%@page import="Business.BlogEntry"%>
<%@page import="DAO.BlogEntryDao"%>
<%@page import="Business.Friendship"%>
<%@page import="java.util.ArrayList"%>
<%@page import="DAO.FriendshipDao"%>
<%@page import="DAO.FriendshipDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    <a href="login.jsp">Logout</a>
</head>
<body>
    <h2>Friends List:</h2>
    <% Object msg = session.getAttribute("login_message");
        String username = (String) msg;
        FriendshipDao friendshipdao = new FriendshipDao("myBlog");
        ArrayList<Friendship> friends = new ArrayList();
        friends = friendshipdao.findFriendshipsByUsername(username);
        BlogEntryDao blogsentrydao = new BlogEntryDao("myBlog");
        ArrayList<BlogEntry> blogs = new ArrayList();
        String friend = "";
        
        for (int i = 0; i < friends.size(); i++) {
            if (friends.get(i).getUser1().getUsername().equals(username)) {
                friend = friends.get(i).getUser2().getUsername();
                blogs = blogsentrydao.findBlogEntriesByAuthor(friend);
            }
            else {
                friend = friends.get(i).getUser1().getUsername();
                blogs = blogsentrydao.findBlogEntriesByAuthor(friend);
            }
    %> 
    <a href="blogInfo.jsp?friendname=<%=friend%>"><%=friend%></a>
    <%
            
        }


    %>
</body>
</html>

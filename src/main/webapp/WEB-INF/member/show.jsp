<%@page import="com.bodejidi.Member"%>
<%@ page pageEncoding="UTF-8"%>
<%Member member = (Member)request.getAttribute("member");%>
<html>
    <head>
        <title>指定会员</title>
    </head>
<body>
    welcome,admin!<a href="/jdbc/auth/logout">logout</a>
    <h1>第<%out.print(member.getId());%>号会员</h1>
    <form action="member" method="POST">
        <table border="2">
            <tr>
                <th>ID</th>
                <th>First Name</th>
                <th>Last Name</th>
            </tr>
            <tr>
                <td><%out.print(member.getId());%></td>
                <td><input type="text" name="first_name" value="<%out.print(member.getFirstName());%>"/></td>
                <td><input type="text" name="last_name" value="<%out.print(member.getLastName());%>"></td>
            </tr>
        </table></br>
        <input type="submit" name="action" value="update">
        <input type="submit" name="action" value="delete">
        <input type="hidden" name="id" value="<%out.print(member.getId());%>">
    </form>
    <a href="?action=create">Add Member</a>
    </body>
</html>
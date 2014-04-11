<%@ page import="java.util.List, com.bodejidi.Member"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
List<Member> memberList = (List<Member>) request.getAttribute("memberList");
%>
<html>
    <head>
        <title>会员管理</title>
    </head>
    <body>
        welcome,admin!<a href="/jdbc/auth/logout">logout</a>
        <h1>会员列表</h1>
        <table border=\"2\">
            <tr>
                <th>ID</th>
                <th>Name</th>
            </tr>
            <c:forEach var="member" items="${memberList}">
            <tr>
                <td><a href="?action=show&id=${member.id}">${member.id}</a></td>
                <td>${member.firstName} ${member.lastName}</td>
            </tr>
            </c:forEach>
        
        </table>
        <p><a href="?action=create">Add member</a></p>
    </body>
</html>
    
         
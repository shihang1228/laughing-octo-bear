<html>
    <head>
        <title>新增会员</title>
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>	
    </head>
    <body>
    <%@ include file="../common/header.jsp"%>
        <h1>新增会员</h1>
        <form action="member" method="POST">
            <label>firstName:<input type="text" name="first_name"/></label></br>
            <label>LastName :<input type="text" name="last_name"/></label></br>
            <input type="hidden" name="action" value="save"/></label></br>
            <input type="submit" name="action" value="提交"/>
        </form>
        <a href="?action=list">Member List</a>
    </body>
</html>
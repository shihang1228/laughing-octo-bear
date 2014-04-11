<html>
    <head>
        <meta http-equiv="refresh" content="3;url=?action=list"/>
    </head>
    <body>
        <%String message = (String)request.getAttribute("flash_massage");
        out.print(message);%><br>
        Please wait for 2 seconds, if not redirect please click <a href="member?action=List">here</a>.       
    </body>
</html>
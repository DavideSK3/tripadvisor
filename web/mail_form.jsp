<%-- 
    Document   : mail_form
    Created on : 15-mag-2016, 14.46.32
    Author     : gabriele
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>AccountRecovery</title>
    </head>
    <body>
        
        <p><c:out value='${requestScope.message}'/></p
        
        <h2>Forgot your account's password? Enter your email address and we'll send you a recovery link.</h2>
        
        <form action ="PasswordRecovery" method="GET">
            <input type="email" name ="mail"><br>
            <input type="submit">
        </form>
    </body>
</html>

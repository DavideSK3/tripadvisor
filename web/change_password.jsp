<%-- 
    Document   : change_password
    Created on : 15-mag-2016, 14.06.29
    Author     : gabriele
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Change Password</title>
    </head>
    <body>
        
        <p><c:out value='${requestScope.message}'/></p>
        
            
        <h1>Salve <c:out value='${user.name}'/> <c:out value='${user.surname}'/></h1>
        <h2>Completa il seguente form per impostare una nuova password</h2>
        
        <form method="POST" action="<c:url value='ValidateChangePassword'/>">
            
            <label for="pw1">Inserisci la nuova password</label><br>
            <input type="password" name ="password1"><br>
            <label for="pw2">Ripeti la password</label><br>
            <input type="password" name ="password2"><br>
            <input type="submit">
        </form>
    </body>
</html>

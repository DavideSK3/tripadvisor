<%-- 
    Document   : restaurant
    Created on : 11-mag-2016, 14.07.39
    Author     : gabriele
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%@include file="header.jsp" %>
        
        <h1>Ristorante <c:out value='${restaurantID}'/> </h1>
        
        <form method="POST" action="Login">
            
            
            <input type="hidden" name="redirectPage" value="<c:out value='${requestScope.redirectURL}'/>">
            <input type="submit" value="Login">
        </form>
            
           
        
        <%@include file="footer.html" %>
    </body>
</html>

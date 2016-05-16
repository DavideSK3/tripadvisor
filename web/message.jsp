<%-- 
    Document   : message
    Created on : 15-mag-2016, 13.56.02
    Author     : gabriele
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%@include file="header.jsp" %>
        
        <p><c:out value='${requestScope.message}'/></p>
        
        <%@include file="footer.html" %>
    </body>
</html>

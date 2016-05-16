<%-- 
    Document   : result_list
    Created on : 11-mag-2016, 14.08.07
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
        
        <h1>Tanti Ristoranti</h1>
        
        
        
        <form method="POST" action="Login">
            
            
            <input type="hidden" name="redirectPage" value="<c:out value='${requestScope.redirectURL}'/>">
            <input type="submit" value="Login">
        </form>
        <table>
        <c:forEach var='r' items="${restaurantsList}">
                <tr>
                    <td>
                        <c:out value="${r.name}"/>

                    </td>
                    <td>
                        <c:out value="${r.address}"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
            
            
        <%@include file="footer.html" %>
        
    </body>
</html>

<%-- 
    Document   : result_list
    Created on : 11-mag-2016, 14.08.07
    Author     : gabriele
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ristoranti Trovati</title>
        <link rel="stylesheet" type="text/css" href="media/css/jquery.dataTables.css">

	<script type="text/javascript" language="javascript" src="media/js/jquery.js"></script>
	<script type="text/javascript" language="javascript" src="media/js/jquery.dataTables.js"></script>	
        <script type="text/javascript" language="javascript" class="init">
            $(document).ready(function() {

                    $('#ristoranti').dataTable();

            } );
        </script>
    </head>
    <body>
        <%@include file="header.jsp" %>
        
        
                
        <table id ="ristoranti" border="1px">
            <thead>
                <th>Nome</th>
                <th>Indirizzo</th>
                <th>Id</th>
            </thead>
        <c:forEach var='r' items="${restaurantsList}">
                <tr>
                    <td>
                        <c:out value="${r.name}"/>

                    </td>
                    <td>
                        <c:out value="${r.address}"/>
                    </td>
                    <td>
                        <c:out value="${r.id}"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
            
            
        <%@include file="footer.html" %>
        
    </body>
</html>

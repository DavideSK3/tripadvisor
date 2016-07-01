<%-- 
    Document   : prova_upload
    Created on : 14-giu-2016, 19.04.52
    Author     : gabriele
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action ="Review" method ="POST" ENCTYPE="multipart/form-data">
            <input type ="hidden" name = "global" value = '5'>
            <input type ="hidden" name="food" value ='5' >
            <input type ="hidden" name="service" value ='5' >
            <input type ="hidden" name="money" value ='5' >
            <input type ="hidden" name="atmosphere" value ='5' >
            <input type ="hidden" name="title" value ='CHE BELLO :)' >
            <input type ="hidden" name="description" value ='faceva schifo... :((((' >
            <input type ="hidden" name="id_restaurant" value ='3' >
            
            
            <input type ="hidden" name ="return_address" value ="prova_upload.jsp">
            <input type ="text" name ="photoName"><br>
            <INPUT TYPE="FILE" NAME="img">
            <input type="submit">
        </form>
    </body>
</html>

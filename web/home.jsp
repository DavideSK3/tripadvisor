
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        
        <%@include file="header_head.jsp" %>
        <title>TripAdvisor</title>
    </head>
    <body style=" background-color: gainsboro">

        <%@include file="header.jsp" %>
        
        <div style="padding:0px; background:url(data/sfondo_restaurant.jpg);background-size: cover;
                                       background-repeat: no-repeat; padding-bottom: 50%;">
            <div class="col-sm-3">
                <%@include file="ricerca.jsp" %>
            </div>
            <div class="col-sm-8" style="text-align: right;">
                <h1 style="color:white;font-family:Arial, Helvetica, sans-serif;">Cerca i migliori ristoranti vicino a te</h1>
            </div>
        </div>

        

        <%@include file="footer.html" %>  
        <%@include file="js_include.jsp" %>
    </body>
</html>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="styles.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
      
        <title>TripAdvisor</title>
    </head>
    <body style=" background-color: gainsboro">

        <%@include file="header.jsp" %>
        
        <div class=" col-md-12" style="padding:0px; background:url(data/sfondo_restaurant.jpg);background-size: cover;
                                       background-repeat: no-repeat; text-align: right; padding-bottom: 35%; ">
            
            <h1 style="color:white;font-family:Arial, Helvetica, sans-serif;">Cerca i migliori ristoranti vicino a te&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</h1>
        </div>

        

        <%@include file="footer.html" %>  


    </body>
</html>

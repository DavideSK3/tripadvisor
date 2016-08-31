<!-- Pagina che visualizza diversi messaggi generati dalle servlets -->
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="header_head.jsp" %>
        <title>Message</title>
    </head>
    <body style="background-color:gainsboro">
        <%@include file="header.jsp" %>
        
        <div class="col-md-6 col-md-offset-3">
            <div class="jumbotron" style="margin-top: 25%; border-radius: 15px">
              <div class="col-md-offset-1">  
                <span style="font-size: 200%"> <b><c:out value='${requestScope.message}'/></b></span>
            </div>
            </div>
        </div>
        
        
        
        <%@include file="footer.html" %>  
        <%@include file="js_include.jsp" %>
    </body>
</html>

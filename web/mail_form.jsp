<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="header_head.jsp" %>
        <title>AccountRecovery</title>
    </head>
    <body style="background-color:gainsboro">
        <%@include file="header.jsp" %>
        
        <c:out value='${requestScope.message}'/>  
        
        <div class="col-md-6 col-md-offset-3">
            <div class="jumbotron" style="margin-top: 20%; border-radius: 15px">
                <div class="col-md-offset-1">  
                    <span style="font-size: 140%; color: limegreen"><b>Forgot your account's password?</b></span><br><br>
                    <span style="font-size: 100%;">Enter your email address and we'll send you a recovery link.</span>
                    <form action ="<c:url value='PasswordRecovery'/>" style="margin-top: 5px;"method="POST">
                        <input type="email" name ="mail" class="form-control" style="width: 80%" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" required autofocus><br>
                        <input type="submit" class="btn btn-default">
                    </form>
                </div>
            </div>
        </div>
        
        
        
        <%@include file="footer.html" %>  
        <%@include file="js_include.jsp" %>
        
    </body>
</html>
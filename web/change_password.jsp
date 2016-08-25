
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="header_head.jsp" %>
        <title>Change Password</title>
    </head>
    <body style="background-color:gainsboro">
        <%@include file="header.jsp" %>
        <div class="col-md-6 col-md-offset-3">
            <div class="jumbotron" style="margin-top: 20%; border-radius: 15px">
                <div class="col-md-offset-1">  
                    
                    <span style="font-size: 140%; color: limegreen"><b>Salve <c:out value='${c_user.name}'/> <c:out value='${c_user.surname}'/></b></span><br>
                    <span style="font-size: 100%;"><b>Completa il seguente form per impostare una nuova password</b></span><br><br>
                    <form method="POST" action="<c:url value='ValidateChangePassword'/>">
                        <input id="password" name="password1" class="form-control" style="width: 80%" type="password" pattern=".{8,}$" title="La password deve contere almeno 8 caratteri alfanumerici"
                           onchange="this.setCustomValidity(this.validity.patternMismatch ? 'La tua password deve essere di almeno 8 caratteri' : ''); 
                               if(this.checkValidity()) form.password_two.pattern = this.value;" 
                               placeholder="Password" required>
                        <input id="password_two" name="password2" style="width: 80%" class="form-control" type="password" pattern=".{8,}$" 
                               onchange="this.setCustomValidity(this.validity.patternMismatch ?'Password diverse' : '');" 
                               placeholder="Conferma Password" required>
                        
                        <input type="submit" class="btn btn-default">
                    </form>
                </div>
            </div>
        </div>
        
        
        
        <%@include file="footer.html" %>  
        <%@include file="js_include.jsp" %>
        
    </body>
</html>
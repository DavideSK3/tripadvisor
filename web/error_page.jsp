
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isErrorPage="true"%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="header_head.jsp" %>
        <title>Errore</title>
    </head>
    <body style="background-color:gainsboro">
        <%@include file="header.jsp" %>
        
        <div class="col-md-6 col-md-offset-3">
            <div class="jumbotron" style="margin-top: 25%; border-radius: 15px; ">
                
                <span style="font-size: 200%; padding-left: 25%"> <b>Something went wrong !</b></span>
                <br><br>
                <button type="button" class="btn btn-info " data-toggle="modal" data-target="#error_message_modal" style="background-color: limegreen; border-color: limegreen; margin-left: 45%;"> Message</button>
                <div class="modal fade" id="error_message_modal" role="dialog">
                    <div class="modal-dialog modal-sm">
                        <div class="modal-content">
                            <div class="modal-header" style="border-bottom-width: 0;">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h3 class="modal-title"> <b>Error :</b></h3>
                            </div>
                            <c:if test="${exception != null}">
                                <div class="modal-body" style="border-radius: 20px; border-top-width: 0;border-bottom-width: 0;">
                                    <%--<%= exception.toString() %>--%>
                                    <c:out value="${exception.toString()}"/>
                                </div>
                            </c:if>
                            
                            
                            <div class="modal-footer" style="border-top-width: 0;">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <%@include file="footer.html" %>  
        <%@include file="js_include.jsp" %>
    </body>
</html>

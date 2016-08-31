<!-- Pagina che permette a un amministratore o ristoratore di visualizzare, 
    accettare o rifiutare tutte le notifiche presenti. -->
   
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="header_head.jsp" %>
        <%@ page import="db.User" %>
        <style>
            .row{
                margin: 0;
            }
        </style>
        <title>Notifiche</title>
    </head>
    
    <body>
        <!-- Header senza campi di ricerca -->
        <nav class="navbar header">
            <div class="container-fluid header" >
                <div class="col-md-4 header">
                    <a class="navbar-brand header" href="<c:url value='/'/>">
                      <img src="data/TripAdvisor_logo.png" class=" header" alt="TripAdvisor"  /> 
                    </a>
                </div>
                <div class="col-md-4 header" style="padding-top: 1.5%">
                    <span>Leggi le recensioni e prenota il miglior ristorante</span>
                </div>
                <div class="col-md-3 header" style=" padding-top: 10px; float: right">
                    <div class="dropdown" style="float:right;">
                        <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown" style="background-color: limegreen; border-color: limegreen;">
                            <c:out value="${sessionScope.user.name} ${sessionScope.user.surname}"/>
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu">    
                            <li><a href="<c:url value='Profile'/>" class="glyphicon glyphicon-user utente"> Profilo </a></li>
                            <li><a href="<c:url value='Logout'/>" class="glyphicon glyphicon-log-out utente"> Logout </a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </nav>
                        
        <div class="row">
            <div class="col-sm-6 col-md-4 col-md-offset-4">
                <h1 class="text-center login-title">Ciao <c:out value="${sessionScope.user.name} ${sessionScope.user.surname}"/>! </h1>
                <img class="profile-img center-block" src="http://www.freeiconspng.com/uploads/msn-people-person-profile-user-icon--icon-search-engine-16.png" alt="logo" style="max-width:360px; max-height: 90px; margin-top: 15px; margin-bottom: 15px; ">
                <br><br>
                <center><h1><c:out value='${requestScope.message}'/></h1></center>
                <br>
            </div>
        </div>
        
        <!-- Mostra notifiche di nuove foto inserite relative ai ristoranti posseduti dal ristoratore
            e permette a questi di segnalarle all'amministratore o approvarle -->
        <c:choose>
            <c:when test="${sessionScope.user.getType() == 'R'}">
            
                <div class="row">
                    <div class="col-sm-6 col-md-4 col-md-offset-4">
                        <h3 class="text-center">Notifiche dai tuoi Ristoranti </h3> <br>
                    </div>
                </div>
                
                <c:forEach var='rf' items="${requestScope.results_foto}">
                   <div class="container-fluid riquadro_ristorante">
                       <div class="col-md-4" style=" padding-left: 1%; padding-top: 1%; padding-bottom: 1%; ">
                         <img src="<c:out value="${rf.path}"/>" class="img-rounded" alt="<c:out value="${rf.photo_name}"/>" style ="max-width: 100%; max-height : 100%; min-width:160px; min-height: 49px;">
                       </div>
                       <div class="col-md-4" style=" padding-left: 1%; padding-top: 1%; padding-bottom: 1%; ">
                            <br>
                            <a href="<c:url value='Restaurant'><c:param name ='restaurantID' value='${rf.id_restaurant}'/></c:url>"/><span style="font-size: 200%; color: royalblue"><b><c:out value="${rf.name}"/></b></span> </a>
                            <br><br>
                            <span style="font-size: 150%"><b>Nome Foto: <c:out value="${rf.photo_name}"/></b></span>
                            <br><br>
                       </div>
                        <div class="col-md-4" style=" padding-left: 1%; padding-top: 1%; padding-bottom: 1%; ">
                            <form action="<c:url value ='NotificationRestaurant'/>" method="POST">
                                <input type="hidden" name="id_photo" value="${rf.id_photo}">
                                <button class="btn btn-lg btn-primary btn-success" type="submit" style="width:150px" name="segnala_foto" value="conferma"> Accetta</button>
                                <br><br>
                                <button class="btn btn-lg btn-primary btn-danger" type="submit" style="width:150px" name="segnala_foto" value="segnala"> Segnala</button>
                            </form>
                            <br>
                        </div>
                   </div>
               </c:forEach>
            </c:when>
        </c:choose>
        
                
        <!-- Mostra notifiche di foto segnalate da qualsiasi ristoratore
            e permette all' amministratore di approvarle o eliminarle -->       
        <c:choose>
            <c:when test="${sessionScope.user.getType() == 'A'}">
            
               
                <div class="row">
                    <div class="col-sm-6 col-md-4 col-md-offset-4">
                        <h3 class="text-center">Notifiche da Ristoratori </h3> <br>
                    </div>
                </div>

                <c:forEach var='rf' items="${requestScope.results_foto}">
                   <div class="container-fluid riquadro_ristorante">
                       <div class="col-md-4" style=" padding-left: 1%; padding-top: 1%; padding-bottom: 1%; ">
                         <img src="<c:out value="${rf.path}"/>" class="img-rounded" alt="<c:out value="${rf.photo_name}"/>" style ="max-width: 100%; max-height : 100%; min-width:160px; min-height: 49px;">
                       </div>
                       <div class="col-md-5" style=" padding-left: 1%; padding-top: 1%; padding-bottom: 1%; ">
                            <br>
                            <a href="<c:url value='Restaurant'><c:param name ='restaurantID' value='${rf.id_restaurant}'/></c:url>"> <span style="font-size: 200%; color: royalblue"><b><c:out value="${rf.name}"/></b></span> </a>
                            <br><br>
                            <span style="font-size: 150%"><b>Nome Foto: <c:out value="${rf.photo_name}"/></b></span>
                            <br><br>
                       </div>
                        <div class="col-md-3" style=" padding-left: 1%; padding-top: 1%; padding-bottom: 1%; ">
                            <form action="<c:url value ='NotificationAdmin'/>" method="POST">
                                <input type="hidden" name="id_photo" value="${rf.id_photo}">
                                <button class="btn btn-lg btn-primary btn-success" type="submit" style="width:150px" name="segnala_foto" value="conferma"> Approva Foto</button>
                                <br><br>
                                <button class="btn btn-lg btn-primary btn-danger" type="submit" style="width:150px" name="segnala_foto" value="elimina"> Elimina Foto</button>
                            </form>
                            <br>
                        </div>
                   </div>
               </c:forEach>

                <div class="row">
                    <div class="col-sm-6 col-md-4 col-md-offset-4">
                        <h3 class="text-center">Notifiche da Utenti </h3> <br>
                    </div>
                </div>

                <!-- Mostra notifiche di reclami per ristoranti richiesti da utenti 
                     e permette all' amministratore di approvarle o eliminarle -->
                <c:forEach var='rr' items="${requestScope.results_reclami}">
                    <div class="container-fluid riquadro_ristorante">
                        <div class="col-md-9" style=" padding-left: 1%; padding-top: 1%; padding-bottom: 1%; ">
                            <a href="<c:url value='Restaurant'><c:param name ='restaurantID' value='${rr.id_restaurant}'/></c:url>"> <span style="font-size: 200%; color: royalblue"><b><c:out value="${rr.restaurant_name}"/></b></span> </a>
                            <br><br>
                            <span style="font-size: 150%">L'utente <b><c:out value="${rr.name}"/> <c:out value="${rr.surname}"/></b> vuole reclamare il ristorante</span>
                            <br><br>
                            <span style="font-size: 150%">Contatto Email:  <b><c:out value="${rr.email}"/></b></span>
                            <br>
                        </div>
                        <div class="col-md-3" style=" padding-left: 1%; padding-top: 1%; padding-bottom: 1%; ">
                            <form action="<c:url value ='NotificationAdmin'/>" method="POST">
                                <br>
                                <input type="hidden" name="id_user" value="${rr.id_user}">
                                <input type="hidden" name="id_restaurant" value="${rr.id_restaurant}">
                                <button class="btn btn-lg btn-primary btn-success" style="width:150px" type="submit" name="reclamo" value="conferma"> Accetta</button>
                                <br><br>
                                <button class="btn btn-lg btn-primary btn-danger" style="width:150px" type="submit" name="reclamo" value="elimina"> Rifiuta</button>
                            </form>
                        </div>
                    </div>
                <br>
                </c:forEach>
        
            </c:when>
        </c:choose>    
        
        
        
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        <%@include file="footer.html" %>
        <%@include file="js_include.jsp" %>
        
    </body>
</html>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         <%@include file="header_head.jsp" %>
         
         <%@ page import="db.User" %>
        <title>Profilo</title>
    </head>
    <body>
        <nav class="navbar header">
            <div class="container-fluid header" >
                <div class="col-md-4 header">
                    <a class="navbar-brand header" href="<c:url value='/TripAdvisor'/>">
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
                            <c:choose>
                                <c:when test="${sessionScope.user.getType() == 'R'}">
                                    <li><a href="<c:url value='NotificationRestaurant'/>" class="glyphicon glyphicon-user utente"> Notifiche</a></li>
                                </c:when>
                                <c:when test="${sessionScope.user.getType() == 'A'}">
                                <li><a href="<c:url value='NotificationAdmin'/>" class="glyphicon glyphicon-user utente"> Notifiche</a></li>
                                </c:when>
                            </c:choose>
                            <li><a href="<c:url value='Logout'/>" class="glyphicon glyphicon-log-out utente"> Logout </a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </nav>
        <div class="row">
            <div class="col-sm-6 col-md-4 col-md-offset-4">
                <h1 class="text-center login-title">Ciao <c:out value="${sessionScope.user.name} ${sessionScope.user.surname}"/>! </h1>
                <h3 class="text-center">Modifica i tuoi dati </h3>
                <div class="account-wall">
                    <img class="profile-img center-block" src="http://www.freeiconspng.com/uploads/msn-people-person-profile-user-icon--icon-search-engine-16.png" alt="logo" style="max-width:360px; max-height: 90px; margin-top: 15px; margin-bottom: 15px; ">
                    
                    <div class="jumbotron jumbotron-fluid" style="padding: 20px 0px; border-radius: 5px;">
                        <div style="padding: 0px 20px;">
                            <span style="font-size: 150%"> Nome :    <c:out value="${sessionScope.user.name}"/></span> <br><br>
                            <span style="font-size: 150%"> Cognome :    <c:out value="${sessionScope.user.surname}"/></span><br><br>
                            <p style="padding: 0px 20px 20px" ><button type="button" class="btn btn-info " data-toggle="modal" data-target="#nome" style="background-color: limegreen; border-color: limegreen;  float:left; "> Modifica Nome e Cognome</button>
                        </p>
                        </div>
                        
                        <br>
                        <div style="padding: 0px 20px;">
                            <span style="font-size: 150%"> Mail : <c:out value="${sessionScope.user.email}"/> </span><br>
                            <p style="padding: 20px 20px 0px" ><button type="button" class="btn btn-info " data-toggle="modal" data-target="#email" style="background-color: limegreen; border-color: limegreen;  float:left; "> Modifica Email</button>
                            &nbsp;<button type="button" class="btn btn-info " data-toggle="modal" data-target="#password" style="background-color: limegreen; border-color: limegreen;  float:left; margin-left: 10px"> Modifica Password</button>
                        </p>
                        </div>
                        
                        <div class="modal fade" id="nome" role="dialog">
                            <div class="modal-dialog modal-sm">
                                <div class="modal-content">
                                    <div class="modal-header" style="border-bottom-width: 0;">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    </div>
                                    <div class="modal-body" style="border-radius: 20px; border-top-width: 0;border-bottom-width: 0;">
                                        <form method='POST' >
                                            <div style="padding-bottom: 20px; ">
                                                <input type="text" name="name" class="form-control" placeholder="<c:out value="${sessionScope.user.name}"/>" required>
                                                <input type="text" name="surname" class="form-control" placeholder="<c:out value="${sessionScope.user.surname}"/>" required>
                                            </div>
                                            <input style="float: right" class="btn btn-default" TYPE='submit'>
                                            <button style="float: left" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                        </form>
                                    </div>
                                    <div class="modal-footer" style="border-top-width: 0;">

                                    </div>
                                </div>
                            </div>
                        </div>                        
                        <div class="modal fade" id="email" role="dialog">
                            <div class="modal-dialog modal-sm">
                                <div class="modal-content">
                                    <div class="modal-header" style="border-bottom-width: 0;">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    </div>
                                    <div class="modal-body" style="border-radius: 20px; border-top-width: 0;border-bottom-width: 0;">
                                        <form method='POST' >
                                            <div style="padding-bottom: 20px; ">
                                                <input type="email" name="email" class="form-control" placeholder="<c:out value="${sessionScope.user.email}"/>" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" required autofocus>
                                            </div>
                                            <input style="float: right" class="btn btn-default" TYPE='submit'>
                                            <button style="float: left" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                        </form>
                                    </div>
                                    <div class="modal-footer" style="border-top-width: 0;">

                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal fade" id="password" role="dialog">
                            <div class="modal-dialog modal-sm">
                                <div class="modal-content">
                                    <div class="modal-header" style="border-bottom-width: 0;">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    </div>
                                    <div class="modal-body" style="border-radius: 20px; border-top-width: 0;border-bottom-width: 0;">
                                        <form method='POST' >
                                            <div style="padding-bottom: 20px; ">
                                            <input id="password" name="vecchiapassword" class="form-control" type="password" placeholder="Vecchia Password" required>
                                            <input id="password" name="nuovapassword" class="form-control" type="password" pattern=".{8,}$" title="La password deve contere almeno 8 caratteri alfanumerici"
                                                   onchange="this.setCustomValidity(this.validity.patternMismatch ? 'La tua password deve essere di almeno 8 caratteri' : ''); 
                                                       if(this.checkValidity()) form.password_two.pattern = this.value;" 
                                                       placeholder="Nuova Password" required>
                                            <input id="password_two" name="confermapassword" class="form-control" type="password" pattern=".{8,}$" 
                                                   onchange="this.setCustomValidity(this.validity.patternMismatch ?'Password diverse' : '');" 
                                                   placeholder="Conferma Nuova Password" required>
                                                </div>
                                            <input style="float: right" class="btn btn-default" TYPE='submit'>
                                            <button style="float: left" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                        </form>
                                    </div>
                                    <div class="modal-footer" style="border-top-width: 0;">

                                    </div>
                                </div>
                            </div>
                        </div> 
                    </div>
                    <c:choose>
                        <c:when test="${sessionScope.user.getType() == 'R'}">
                            <h3 class="text-center">Gestisci i tuoi Ristoranti </h3> <br>
                        </c:when>
                    </c:choose>
                </div>
            </div>
        </div>
                
         <c:forEach var='r' items="${requestScope.results}">
            <div class="container-fluid riquadro_ristorante">
                <div class="col-md-4" style=" padding-left: 1%; padding-top: 1%; padding-bottom: 1%; ">
                  <img src="data/sfondo_restaurant.jpg" class="img-rounded" alt="<c:out value="${r.name}"/>" style ="max-width: 100%; max-height : 100%; min-width:160px; min-height: 49px;">
                </div>
                <br>
                <a href="<c:url value='Restaurant'><c:param name ='restaurantID' value='${r.id}'/></c:url>"> <span style="font-size: 200%; color: royalblue"><b><c:out value="${r.name}"/></b></span> </a>
                <br><br>
                <span class="badge"><c:out value='${r.global_review}'/>&nbsp;<span class="glyphicon glyphicon-star-empty"></span>&nbsp; su <c:out value='${r.review_count}'/> recensioni</span>
                <br><br>
                <c:out value='${r.address}'/>, <c:out value='${r.city}'/>, <c:out value='${r.region}'/>, <c:out value='${r.state}'/>
                <br><br>
                &nbsp;&nbsp&nbsp<a href="<c:url value='ManageRestaurant'><c:param name ='restaurantID' value='${r.id}'/></c:url>" class="glyphicon glyphicon-wrench utente"> Modifica Informazioni</a>
            </div>
        </c:forEach>
        
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        <%@include file="footer.html" %>
        <%@include file="js_include.jsp" %>
        
    </body>
</html>
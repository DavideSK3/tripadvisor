



<%--
<!DOCTYPE html>
<html>
    
    <head>
         <!-- This meta tags makes sure accents and other special caracters are displayed correctly -->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="styles.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        
        
        <link href="http://code.jquery.com/ui/1.11.4/themes/redmond/jquery-ui.css" rel="stylesheet">
        
    </head>
    <body style=" background-color: gainsboro"> --%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        
        <nav class="navbar header">
            <div class="container-fluid header" >
                    
                <div class="col-md-4 header">
                    <a class="navbar-brand header" href="<c:out value='${pageContext.servletContext.contextPath}'/>">
                      <img src="data/TripAdvisor_logo.png" class=" header" alt="TripAdvisor"  /> 
                    </a>
                </div>
                
                <div class="col-md-4 header" style="padding-top: 1.5%">
                    <span>Leggi le recensioni e prenota il miglior ristorante</span>
                </div>
                
                      
                <c:choose>
		    <c:when test="${sessionScope.user == null}">
			<div class="col-md-4 header">
                            <ul class="nav navbar-nav navbar-right header " >
                                <li><a href="<c:url value='login.html'/>" class="glyphicon glyphicon-log-in utente "> Accedi </a></li>
                                <li><a href="<c:url value='register.html'/>" class="glyphicon glyphicon-user utente">  Registrati</a></li>
                            </ul>
                        </div>
		    </c:when>
                    <c:when test="${sessionScope.user.type == 'U'}">
                        <div class="col-md-3 header" style=" padding-top: 10px; float: right">
                            <div class="dropdown" style="float:right;">
                                <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown" style="background-color: limegreen; border-color: limegreen;">
                                    <c:out value="${sessionScope.user.name} ${sessionScope.user.surname}"/>
                                    <span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a href="<c:url value='Profile'/>" class="glyphicon glyphicon-user utente"> Profilo</a></li>
                                  <li><a href="<c:url value='Logout'/>" class="glyphicon glyphicon-log-out utente"> Logout </a></li>
                                </ul>
                            </div>
                        </div>
                    </c:when>
		    <c:otherwise>
                        <div class="col-md-3 header" style=" padding-top: 10px; float: right">
                            
                            <div class="dropdown" style="float:right;">
                                <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown" style="background-color: limegreen; border-color: limegreen;">
                                    <c:out value="${sessionScope.user.name} ${sessionScope.user.surname}"/>
                                <span class="caret"></span></button>
                                <ul class="dropdown-menu">
                                  <li><a href="<c:url value='Profile'/>" class="glyphicon glyphicon-user utente"> Profile</a></li>
                                  <li><a href="<c:url value='Logout'/>" class="glyphicon glyphicon-log-out utente"> Logout </a></li>
                                </ul>
                            </div>
                                
                            <button type="button" class="btn btn-info " data-toggle="modal" data-target="#notificationModal" style="background-color: limegreen; border-color: limegreen; float: right;">
                                Notifiche
                            </button>
                            <div class="modal fade" id="notificationModal" role="dialog">
                              <div class="modal-dialog modal-sm">
                                <div class="modal-content">
                                  <div class="modal-header" style="border-bottom-width: 0;">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">Notifiche</h4>
                                  </div>
                                  <div class="modal-body" style="background-color: gainsboro; border-radius: 20px; border-top-width: 0;border-bottom-width: 0;">
                                    <p>Questa è una notifica.</p>
                                  </div>
                                  <div class="modal-footer" style="border-top-width: 0;">
                                      <span><a href="<c:url value='#'/>" class="glyphicon glyphicon-plus" style="float:left; top: 10px;"> Vedi tutte</a></span>
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                  </div>
                                </div>
                              </div>
                            </div>
                        </div>
		    </c:otherwise>
		</c:choose>
                
            </div>
        </nav>
        <div class="navbar search">
              <div class="container-fluid" >
                  <div class="row">
                      <form class="navbar-search" role="search" action="<c:url value='RestaurantsList'/>" method ="GET" id = "search_form">

                          <div class=" col-md-5 col-sm-5"><input type="text" class="form-control" placeholder="Dove vai?" name="place" id ="search_place" value="<c:out value='${requestScope.place}'/>"></div>
                          <div class=" col-md-5 col-sm-5 ui-widget"> <input type="text" class="form-control" placeholder="Ricerca ristorante" name="r_query" id ="search_name" value="<c:out value='${requestScope.r_query}'/>"></div>
                          <div class=" col-md-2 col-sm-5"> <button type="submit" name="button" value="Search" class="btn btn-default"> <span class="glyphicon glyphicon-search" style="color: black"></span> </button></div>

                      </form>
                  </div>
              </div>
        </div>
        
        
    <%--</body>
    
</html>--%>
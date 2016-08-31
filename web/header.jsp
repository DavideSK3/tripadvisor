<!-- Header -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



        <!-- barra in alto contenente il logo e i pulsanti per accesso/ registrazione oppure per la gestione 
            del proprio profilo e delle notifiche (in caso di amministratore o ristoratore) -->
        <nav class="navbar header">
            <div class="container-fluid header" >
                
                <!-- Immagine del sito -->
                <div class="col-md-4 header" style="min-height: 50px">
                    <a class="navbar-brand header" href="<c:url value='/'/>">
                      <img src="data/TripAdvisor_logo.png" class="header" alt="TripAdvisor"  /> 
                    </a>
                </div>
                
                <!-- Descrizione del sito -->
                <div class="col-md-4 header" style="padding-top: 1.5%">
                    <span>Leggi le recensioni e prenota il miglior ristorante</span>
                </div>
                
                <!-- A seconda del tipo di utente vengono mostrati bottoni diversi:
                     - Utente Anonimo : Accedi e Registrati
                     - Utente Registrato : Profilo
                     - Ristoratore e amministatore : Notifiche e Profilo
                -->    
                <c:choose>
		    
                    
		    <c:when test="${sessionScope.user == null}">
                        <!-- utente anonimo -->
			<div class="col-md-4 header">
                            <ul class="nav navbar-nav navbar-right header " >
                                <li><a href="<c:url value='login.html'/>" class="glyphicon glyphicon-log-in utente "> Accedi </a></li>
                                <li><a href="<c:url value='register.html'/>" class="glyphicon glyphicon-user utente">  Registrati</a></li>
                            </ul>
                        </div>
		    </c:when>
                    
                    
                    <c:when test="${sessionScope.user.type == 'U'}">
                        <!-- utente registrato --> 
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
                        <!-- utente amministratore o ristoratore -->
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
                                
                            <!-- bottone che mostra una finestra con la prima notifica e da la possibilità  di accedere alla pagina con tutte le notifiche -->
                            <button type="button" class="btn btn-info " data-toggle="modal" data-target="#notificationModal" style="background-color: limegreen; border-color: limegreen; float: right;">
                                Notifiche
                            </button>
                            <div class="modal fade" id="notificationModal" role="dialog">
                              <div class="modal-dialog modal-sm">
                                <div class="modal-content">
                                  <div class="modal-header" style="border-bottom-width: 0;">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">Anteprima Notifiche</h4>
                                  </div>
                                  <div class="modal-body" style="background-color: gainsboro; border-radius: 20px; border-top-width: 0;border-bottom-width: 0;">
                                    <c:if test="${notificaFoto != null}">
                                        <c:choose>
                                            <c:when test="${sessionScope.user.getType() == 'R'}">
                                                <p>È stata inserita una foto nel ristorante <c:out value='${notificaFoto.name}'/></p>
                                            </c:when>
                                            <c:when test="${sessionScope.user.getType() == 'A'}">
                                                <p>È stata segnalata una foto nel ristorante <c:out value='${notificaFoto.name}'/></p>
                                            </c:when>
                                        </c:choose>
                                    </c:if>
                                    <c:if test="${notificaReclamo != null}">
                                        <p>L'utente <c:out value="${notificaReclamo.name}"/> <c:out value="${notificaReclamo.surname}"/> vorrebbe reclamare il ristorante <c:out value='${notificaReclamo.restaurant_name}'/></p>
                                    </c:if>
                                    <c:if test="${notificaReclamo == null && notificaFoto == null}">
                                        <p>Non ci sono notifiche da mostrare.</p>
                                    </c:if>
                                  </div>
                                  <div class="modal-footer" style="border-top-width: 0;">
                                    <span>
                                          <c:choose>
                                              <c:when test="${sessionScope.user.getType() == 'R'}">
                                                  <a href="<c:url value='NotificationRestaurant'/>" class="glyphicon glyphicon-plus" style="float:left; top: 10px;">Vedi tutte le <c:out value='${numeroNotifiche}'/> notifiche</a>
                                              </c:when>
                                              <c:when test="${sessionScope.user.getType() == 'A'}">
                                                  <a href="<c:url value='NotificationAdmin'/>" class="glyphicon glyphicon-plus" style="float:left; top: 10px;"> Vedi tutte  le <c:out value='${numeroNotifiche}'/> notifiche</a>
                                              </c:when>
                                          </c:choose>
                                    </span>
                                      <br><br>
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Chiudi</button>
                                  </div>
                                </div>
                              </div>
                            </div>
                        </div>
		    </c:otherwise>
		</c:choose>
                
            </div>
        </nav>
                      
                      
        <!-- form per la ricerca di luoghi e ristoranti -->
        <div class="navbar search">
              <div class="container-fluid" >
                  <div class="row">
                      <form class="navbar-search" role="search" action="<c:url value='RestaurantsList'/>">

                          <div class=" col-md-5 col-sm-5"><input type="text" class="form-control" placeholder="Dove vai?" name="place" id ="search_place" value="<c:out value='${requestScope.place}'/>"></div>
                          <div class=" col-md-5 col-sm-5 ui-widget"> <input type="text" class="form-control" placeholder="Ricerca ristorante" name="r_query" id ="search_name" value="<c:out value='${requestScope.r_query}'/>"></div>
                          <div class=" col-md-2 col-sm-5"> <button type="submit" name="button" value="Search" class="btn btn-default"> <span class="glyphicon glyphicon-search" style="color: black"></span> </button></div>

                      </form>
                  </div>
              </div>
        </div>
        

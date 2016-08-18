<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <!-- This meta tags makes sure accents and other special caracters are displayed correctly -->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="header_head.jsp"%>
        <title>TripAdvisor</title>
    </head>
    
    <body style=" background-color: gainsboro">
        <nav class="navbar header">
            <div class="container-fluid header" >
                <div class="col-md-4 header">
                    <a class="navbar-brand header" href="<c:out value='${pageContext.servletContext.contextPath}'/>">
                      <img src="TripAdvisor_logo.png" class=" header" alt="TripAdvisor"  /> 
                    </a>
                </div>
                <div class="col-md-4 header" style="padding-top: 1.5%">
                    <span>Leggi le recensioni e prenota il miglior ristorante</span>
                </div>
                <div class="col-md-3 header" style=" padding-top: 10px; float: right">
                    <div class="dropdown" style="float:right;">
                        <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown" style="background-color: limegreen; border-color: limegreen;">Erminio Ottone
                        <span class="caret"></span></button>
                        <ul class="dropdown-menu">
                          <li><a href="<c:url value='Profile'/>" class="glyphicon glyphicon-user utente"> Profilo</a></li>
                          <li><a href="<c:url value='Logout'/>" class="glyphicon glyphicon-log-out utente"> Logout </a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </nav>
        <div class="row">
            <div class="col-md-10 col-md-offset-1">
                <h1 class="text-center login-title">Notifiche Amministratore</h1>
            </div>
        </div>
                
            
        <div class="col-md-10 col-md-offset-1"> 
            
            <c:forEach var='n' items='${notifiche}'>
                <div class="row" style="background-color: #999; border-radius: 10px">
                    <div class="col-md-3" style="padding: 10px;">
                        <img src="<c:out value='${n.path}'/>" class="img-rounded" alt="<c:out value='${n.photo_name}'/>" style ="max-width: 100%; max-height : 100%; min-width:160px; min-height: 49px;"> 
                    </div>
                    <div class="col-md-6" style="padding-top: 5%; padding-left: 0px; padding-right: 0px;">
                        <span style="font-size: 150%; color: #000">Il ristorante "<c:out value='${n.name}'/>" ha segnalato questa foto</span>
                    </div>
                    <div class="col-md-1 col-md-offset-1">
                        <form method = "POST">
                            <input type="hidden" name ="photo_id" value ="<c:out value='${n.id_photo}'/>">
                            <button type="submit" name = "accetta" class="btn btn-success " style="padding:15%; margin-top: 50%;"> Accetta</button>
                            <button type="submit" name = "rifuta" class="btn btn-danger " style=" padding:15%; margin-top: 10%;"> Segnala</button>
                        </form>
                        
                    </div>
                </div>
                <br>
            </c:forEach>
            
        </div>
        
        
    </body>
</html>
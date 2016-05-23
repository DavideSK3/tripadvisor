
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    
    <head>
        <!-- This meta tags makes sure accents and other special caracters are displayed correctly -->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <link rel="stylesheet" href="styles.css">
        
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
        
        <link href="http://code.jquery.com/ui/1.11.4/themes/redmond/jquery-ui.css" rel="stylesheet">
        
    </head>
    
    <body>
        <nav class="navbar header">
            <div class="container-fluid header" >
                    
                <a class="navbar-brand header" href="<c:out value='${pageContext.servletContext.contextPath}'/>">
                      <img src="data/TripAdvisor_logo.png" class=" header" alt="TripAdvisor"  /> 
                    </a>
                
                <div class="col-md-4 header">
                    <span>Leggi le recensioni e prenota il miglior ristorante</span>
                </div>
                
		<c:choose>
		    <c:when test="${sessionScope.user != null}">
			
			<ul class="nav navbar-nav navbar-right header">
		            <li> <label class="utente"> Benvenuto, <c:out value="${sessionScope.user.name}"/> &nbsp;&nbsp; </label> </li>
		            <li><a href="#" class="glyphicon glyphicon-user utente">  Profilo</a></li>
		            <li><a href="Logout" class="glyphicon glyphicon-log-out utente"> Logout </a></li>
		        </ul>

		    </c:when>
		    <c:otherwise>
			<ul class="nav navbar-nav navbar-right header ">
                            <form method="POST" action ="Login">
                                <input type="hidden" name="redirectPage" value="<c:out value='${pageContext.request.requestURL}'/>">
                                <li><input type="text" class="form-control" placeholder="E-mail" name="email" ></li>
                                <li><input type="password" class="form-control" placeholder="Password" name="password"></li>
                                <li><input type="submit" value="Accedi"></li>
                            </form>
                            <li><a href="mail_form.jsp">Ho dimenticato la password</a></li>
		            <li><a href="registrazione.html" class="glyphicon glyphicon-user utente">  Registrati</a></li>
		        </ul>		
			
		
		    </c:otherwise>
		</c:choose>
                
            </div>
        </nav>
        
        
        <div class="navbar search">
              <div class="container-fluid" >
                  <div class="row">
                      <form class="navbar-search" role="search" action="RestaurantsList">

                          <div class=" col-md-5"><input type="text" class="form-control" placeholder="Dove vai?" name="place"></div>
                          <div class=" col-md-5 ui-widget ui-widget"> <input type="text" class="form-control" placeholder="Ricerca ristorante" name="restaurant" id ="search_name"></div>
                          <div class=" col-md-2"> <button type="submit" class="btn btn-default"> <span class="glyphicon glyphicon-search" style="color: black"></span> </button></div>

                      </form>
                  </div>
              </div>
        </div>
                      

        <script src="http://code.jquery.com/jquery-2.2.3.js" integrity="sha256-laXWtGydpwqJ8JA+X9x2miwmaiKhn8tVmOVEigRNtP4=" crossorigin="anonymous"></script>
        <script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js" integrity="sha256-DI6NdAhhFRnO2k51mumYeDShet3I8AKCQf/tf7ARNhI=" crossorigin="anonymous"></script>
        <script src="js/autocomplete.js"></script> 
    </body>
</html>
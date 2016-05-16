
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    
    <head>
        <!-- This meta tags makes sure accents and other special caracters are displayed correctly -->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="styles.css">
        
        <title>TripAdvisor</title>
    </head>
    
    <body>
        <nav class="navbar header">
            <div class="container-fluid header" >
                    
                    <a class="navbar-brand header" href="#">
                      <img src="TripAdvisor_logo.png" class=" header" alt="TripAdvisor"  /> 
                    </a>
                
                <div class="col-md-4 header">
                    <span>Leggi le recensioni e prenota il miglior ristorante</span>
                </div>
                
		<c:choose>
		    <c:when test="${sessionScope.user != null}">
			
			<ul class="nav navbar-nav navbar-right header">
		            <li> <label class="utente"> Benvenuto, <c:out value="${sessionScope.user.username}"/> &nbsp;&nbsp; </label> </li>
		            <li><a href="#" class="glyphicon glyphicon-user utente">  Profilo</a></li>
		            <li><a href="Logout" class="glyphicon glyphicon-log-out utente"> Logout </a></li>
		        </ul>

		    </c:when>
		    <c:otherwise>
			<ul class="nav navbar-nav navbar-right header ">
                            <form method="POST" action ="Login">
                                <input type="hidden" name="redirectPage" value="<c:out value='${pageContext.request.requestURL}'/>">
                                <li><input type="text" class="form-control" placeholder="E-mail" ></li>
                                <li><input type="password" class="form-control" placeholder="Password" ></li>
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
                      <form class="navbar-search" role="search" >

                          <div class=" col-md-5"><input type="text" class="form-control" placeholder="Dove vai?"></div>
                          <div class=" col-md-5"> <input type="text" class="form-control" placeholder="Ricerca ristorante"></div>
                          <div class=" col-md-2"> <button type="submit" class="btn btn-default"> <span class="glyphicon glyphicon-search" style="color: black"></span> </button></div>

                      </form>
                  </div>
              </div>
        </div>
    </body>
</html>
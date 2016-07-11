<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ristoranti Trovati</title>
        
        
        <%@include file="header_head.jsp" %>
        

        <!--<link rel="stylesheet" type="text/css" href="media/css/jquery.dataTables.css">-->
        
        <script>
            function degToRad(a){
                return Math.PI * a /180.0;
            }
            var y = document.getElementById("error");
            function getLocation() {
                if (navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(showPosition);
                } else {
                    //y.innerHTML = "Geolocation is not supported by this browser.";
                    //document.getElementById("min_max").disabled = true;
                }
            }
            function showPosition(position) {
                document.getElementById("lat").value = degToRad(position.coords.latitude);
                document.getElementById("long").value = degToRad(position.coords.longitude); 
            }
        </script>
	
        
    </head>
    <body style=" background-color: gainsboro">
        
       <%@include file="header.jsp" %>
        
       <div class="container-fluid" style=" padding-top: 0px;">
            <div class="row">
                <div class="col-sm-3">
                    <div class="sidebar-nav"  >
                        <nav class="navbar navbar-default" role="navigation" style="border-radius: 20px; margin-top: 3%;">
                            <div class = "collapse navbar-collapse" id = "example-navbar-collapse">
                                <form action="AdvancedResearch" method="Post">
                                    <ul class="nav navbar-nav">
                                        <li><input type="text" class="form-control" placeholder="Dove vai?" name="place" id ="advanced_search_place" value="<c:out value='${requestScope.place}'/>"></li>
                                        <li><input type="text" class="form-control" placeholder="Ricerca ristorante" name="r_query" id ="advanced_search_name" value="<c:out value='${requestScope.r_query}'/>"></li>
                                        

                                        <li><label style="padding-left: 5%; padding-top: 2%; font-size: 150%">Ricerca avanzata:</label>
                                        <li><label style="padding-left: 4%;">Range di prezzo:</label>
                                            <label style="padding-left: 5%;"> Min : &nbsp;<input type="number" class="form-control" id= "min_max"   style="max-width: 30%" name ="min_price" value ="<c:out value='${requestScope.min_price}'/>"/></label>
                                            <label style="padding-left: 5%;"> Max : &nbsp;<input type="number" class="form-control" id= "min_max"  style="max-width: 30%" name ="max_price"  value ="<c:out value='${requestScope.max_price}'/>"/></label>
                                        </li>
                                        <li><label style="padding-left: 4%; ">Categorie:</label>
                                              <!--<form style="padding-left: 10%">-->
                                              <c:forEach var ='c' items ='${cuisines}'>
                                                  <input type="checkbox" name= "cusines" value="<c:out value='${c}'/>" <c:if test='${requestScope[c] == true}'>checked</c:if>>
                                                  <c:out value='${c}'/><br>
                                              </c:forEach>
                                        </li>
                                        <li><label style="padding-left: 4%;">Valutazione:</label>
                                              <!--<form style="padding-left: 10%">-->
                                                  <input type="checkbox" name="valutazione" value="5" <c:if test='${requestScope.v5 == true}'>checked</c:if>> 5<span class="glyphicon glyphicon-star-empty"></span>
                                                  <input type="checkbox" name="valutazione" value="4" <c:if test='${requestScope.v4 == true}'>checked</c:if>> 4<span class="glyphicon glyphicon-star-empty"></span>
                                                  <input type="checkbox" name="valutazione" value="3" <c:if test='${requestScope.v3 == true}'>checked</c:if>> 3<span class="glyphicon glyphicon-star-empty"></span>
                                                  <input type="checkbox" name="valutazione" value="2" <c:if test='${requestScope.v2 == true}'>checked</c:if>> 2<span class="glyphicon glyphicon-star-empty"></span>
                                                  <input type="checkbox" name="valutazione" value="1" <c:if test='${requestScope.v1 == true}'>checked</c:if>> 1<span class="glyphicon glyphicon-star-empty"></span>
                                              <!--</form>-->
                                        </li>
                                        <li id ="gl_distance_form" onclick="getLocation()">
                                            <label style="padding-left: 4%;" id ="error"></label>
                                            <label style="padding-left: 4%;">Distanza massima:</label>
                                            
                                            <label style="padding-left: 5%;"> Max : &nbsp;<input type="number" class="form-control" id= "min_max" onclick="getLocation()"  style="max-width: 30%" name ="distance" value = "<c:out value='${requestScope.distance}'/>"/></label>
                                        </li>
                                        <li>
                                            <input type="submit" value ="Search">
                                        </li>
                                        <input type="hidden" name ="longitude" id ="long">
                                        <input type="hidden" name ="latitude" id ="lat">

                                    </ul>
                                </form>
                            </div>
                        </nav>
                    </div>
                </div>
                <div class="col-sm-9" id="centro">
                    <div class="container-fluid" style ="padding-top: 1%" >
                        
                        <label> Ordina per : </label>
                        <!--<button class="btn-lg" style= "background-color: limegreen" data-sort-by="prezzo">Prezzo</button>
                        <button class="btn-lg" style= "background-color: limegreen" data-sort-by="posizione">Posizione in classifica</button>
                        <button class="btn-lg" style= "background-color: limegreen" data-sort-by="distanza">Alfabetico</button>-->
                        <form action = "RestaurantsList" method="GET">
                            <input type="hidden" name ="r_query" value ="<c:out value='${r_query}'/>">
                            <input type="hidden" name ="place" value ="<c:out value='${place}'/>">
                            <input type="hidden" name ="order" value ="price">
                            <button class="btn-lg" style= "background-color: limegreen">Prezzo</button>
                        </form>
                        <form action = "RestaurantsList" method="GET">
                            <input type="hidden" name ="r_query" value ="<c:out value='${requestScope.r_query}'/>">
                            <input type="hidden" name ="place" value ="<c:out value='${requestScope.place}'/>">
                            <input type="hidden" name ="order" value ="name">
                            <button class="btn-lg" style= "background-color: limegreen">Alfabetico</button>
                        </form>
                        <form action = "RestaurantsList" method="GET">
                            <input type="hidden" name ="r_query" value ="<c:out value='${requestScope.r_query}'/>">
                            <input type="hidden" name ="place" value ="<c:out value='${requestScope.place}'/>">
                            <input type="hidden" name ="order" value ="position">
                            <button class="btn-lg" style= "background-color: limegreen">Posizione in classifica</button>
                        </form>
                    </div>
                    <c:if test='${results.size() == 0}'>
                        <div class="container-fluid riquadro_ristorante">
                            
                            
                            <div class="col-md-8">
                                <span style="color: green;"><b>Spiacenti, nessun risultato trovato</b></span><br>
                                <div>
                                    <span class="badge" style="margin-top:8px;">Prova a cercare una zona diversa, effettuare una ricerca meno specifica o controllare l'ortografia</span>
                                </div>
                                <br>
                            </div>
                        </div>
                    </c:if>
                    <c:forEach var='r' items="${results}">
                        <div class="container-fluid riquadro_ristorante">
                            <div class="col-md-4" style=" padding-left: 1%; padding-top: 1%; padding-bottom: 1%; ">
                              <img src="data/sfondo_restaurant.jpg" class="img-rounded" alt="<c:out value="${r.name}"/>" style ="max-width: 100%; max-height : 100%; min-width:160px; min-height: 49px;">
                            </div>
                            <a href="Restaurant?restaurantID=<c:out value="${r.id}"/>"> <span style="font-size: 200%; color: royalblue"><b><c:out value="${r.name}"/></b></span> </a>
                            <div class="col-md-8">
                                <span style="color: green;"><b>N. 1 dei ristoranti in Italia</b></span><br>
                                <div>
                                    <c:forEach var='i' begin='1' end='${r.global_review}' step='1'>
                                        <span class="glyphicon glyphicon-star media"></span>
                                    </c:forEach>
                                    <c:forEach var='i' begin='${r.global_review + 1}' end ='5' step='1'>
                                        <span class="glyphicon glyphicon-star-empty media"></span>
                                    </c:forEach>
                                    &nbsp;
                                    <span class="badge" style="margin-top:8px;"><c:out value="${r.global_review}"/>&nbsp;<span class="glyphicon glyphicon-star-empty"></span>&nbsp; su <c:out value="${r.review_count}"/> recensioni</span>
                                </div>
                                <br>
                                <div style="margin-top:-10px;">
                                    <a href="#"><span class="glyphicon glyphicon-map-marker"></span>&nbsp; Mappa &nbsp;</a>|&nbsp;
                                    <span>Prezzo: <b><c:out value="${r.min_price}"/> € - <c:out value="${r.max_price}"/> €</b></span><br>
                                </div>
                                <div style="margin-top:10px;">
                                    <span class="glyphicon glyphicon-cutlery"></span>&nbsp;
                                    <span>Cucina:</span>&nbsp; 
                                    <c:forEach var='c' items="${r.cuisines}">
                                        <span class="btn btn-success" style="background-color:limegreen; color: white;"><c:out value="${c}"/></span>&nbsp; 
                                    </c:forEach>
                                    
                                    
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    
                    
                </div>
            </div>
        </div>
        
        <div class="container-fluid">  
            <form method="POST" action="<c:out value='${redirectURL}'/>">
                <ul class="pager">
                    <input type="hidden" name="query_id" value ="<c:out value='${query_id}' />">
                    <input type="hidden" name="page" value ="<c:out value='${page}'/>">
                    <input type="hidden" name ="min_price" value ="<c:out value='${requestScope.min_price}'/>">
                    <input type="hidden" name ="max_price" value ="<c:out value='${requestScope.max_price}'/>">
                    <c:forEach var ='c' items ='${cuisines}'>
                        <c:if test='${requestScope[c] == true}'>
                            <input type="hidden" name= "cusines" value="<c:out value='${c}'/>" >
                        </c:if>
                    </c:forEach>  
                    <c:if test='${requestScope.v5 == true}'><input type="hidden" name="valutazione" value="5"></c:if>
                    <c:if test='${requestScope.v4 == true}'><input type="hidden" name="valutazione" value="4"></c:if>
                    <c:if test='${requestScope.v3 == true}'><input type="hidden" name="valutazione" value="3"></c:if>
                    <c:if test='${requestScope.v2 == true}'><input type="hidden" name="valutazione" value="2"></c:if>
                    <c:if test='${requestScope.v1 == true}'><input type="hidden" name="valutazione" value="1"></c:if>
                            
                    <input type="hidden" name ="distance" value = "<c:out value='${requestScope.distance}'/>" >
                    
                    <li><input type="submit" style="background-color:limegreen; color: black;" name="changePageButton" value="Previous"></li>
                    <li><input type="submit" style="background-color:limegreen; color: black;" name="changePageButton" value="Next"></li>
                </ul>
            
            </form>
            <%--<ul class="pager">
                <li><a href= "
                       <c:if test="${page > 0}">
                           <c:out value='${redirectURL}${"&page="}${page-1}'/>
                       </c:if>"
                       style="background-color:limegreen; color: black;">Previous</a></li>
                
              
              <li><a href="<c:out value='${redirectURL}${"&page="}${page+1}'/>" style="background-color:limegreen; color: black;">Next</a></li>
            </ul>--%>
        </div>
       
        <%--<table id ="ristoranti" border="1px">
            <thead>
                <th>Nome</th>
                <th>Indirizzo</th>
                <th>Id</th>
            </thead>
        <c:forEach var='r' items="${restaurantsList}">
                <tr>
                    <td>
                        <a href="Restaurant?restaurantID=<c:out value="${r.id}"/>"><b><c:out value="${r.name}"/></b></a>

                    </td>
                    <td>
                        <c:out value="${r.address}"/>
                    </td>
                    <td>
                        <c:out value="${r.id}"/>
                    </td>
                </tr>
            </c:forEach>
        </table>--%>
        
        
            
        <%@include file="footer.html" %>
        
        
       
        <%--<script type="text/javascript" language="javascript" src="media/js/jquery.js"></script>
	<script type="text/javascript" language="javascript" src="media/js/jquery.dataTables.js"></script>	
        <script type="text/javascript" language="javascript" class="init">
            $(document).ready(function() {

                    $('#ristoranti').dataTable();

            } );
        </script>--%>
         <%@include file="js_include.jsp" %>
        
    </body>
</html>

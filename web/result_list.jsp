<%-- 
    Document   : result_list
    Created on : 11-mag-2016, 14.08.07
    Author     : gabriele
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ristoranti Trovati</title>
        
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="styles.css">
        
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        
        
        <!--<link rel="stylesheet" type="text/css" href="media/css/jquery.dataTables.css">
        
	<script type="text/javascript" language="javascript" src="media/js/jquery.js"></script>
	<script type="text/javascript" language="javascript" src="media/js/jquery.dataTables.js"></script>	
        <script type="text/javascript" language="javascript" class="init">
            $(document).ready(function() {

                    $('#ristoranti').dataTable();

            } );
        </script>-->
    </head>
    <body style=" background-color: gainsboro">
        <%@include file="header.jsp" %>
        
        <div class="container-fluid" style=" padding-top: 0px;">
            <div class="row">
                <div class="col-sm-3">
                    <div class="sidebar-nav"  >
                        <nav class="navbar navbar-default" role="navigation" style="border-radius: 20px; margin-top: 3%;">
                            <div class = "collapse navbar-collapse" id = "example-navbar-collapse">

                                <ul class="nav navbar-nav">
                                    <li><label style="padding-left: 5%; padding-top: 2%; font-size: 150%">Ricerca avanzata:</label>
                                    <li><label style="padding-left: 4%;">Range di prezzo:</label>
                                        <label style="padding-left: 5%;"> Min : &nbsp;<input type="number" class="form-control" id= "min_max"   style="max-width: 30%"/></label>
                                        <label style="padding-left: 5%;"> Max : &nbsp;<input type="number" class="form-control" id= "min_max"  style="max-width: 30%" /></label>
                                    </li>
                                    <li><label style="padding-left: 4%; ">Categorie:</label>
                                          <form style="padding-left: 10%">
                                              <input type="checkbox" name="italiano" >Italiano<br>
                                              <input type="checkbox" name="giapponese" >Giapponese<br>
                                              <input type="checkbox" name="cinese" >Cinese<br>
                                              <input type="checkbox" name="steakhouese" >SteakHouse<br>
                                              <input type="checkbox" name="messicano" >Messicano<br>
                                              <input type="checkbox" name="europeo" value="No">Europeo<br>
                                          </form>
                                    </li>
                                    <li><label style="padding-left: 4%;">Valutazione:</label>
                                          <form style="padding-left: 10%">
                                              <input type="checkbox" name="5stelle" > 5<span class="glyphicon glyphicon-star-empty"></span>
                                              <input type="checkbox" name="4stelle" > 4<span class="glyphicon glyphicon-star-empty"></span>
                                              <input type="checkbox" name="3stelle" > 3<span class="glyphicon glyphicon-star-empty"></span>
                                              <input type="checkbox" name="2stelle" > 2<span class="glyphicon glyphicon-star-empty"></span>
                                              <input type="checkbox" name="1stella" > 1<span class="glyphicon glyphicon-star-empty"></span>
                                          </form>
                                    </li>
                                    <li><label style="padding-left: 4%;">Distanza massima:</label>
                                        <label style="padding-left: 5%;"> Max : &nbsp;<input type="number" class="form-control" id= "min_max"   style="max-width: 30%"/></label>
                                    </li>
                          
                                </ul>
                            </div>
                        </nav>
                    </div>
                </div>
                <div class="col-sm-9" id="centro">
                    <div class="container-fluid" style ="padding-top: 1%" >
                        
                        <label> Ordina per : </label>
                        <button class="btn-lg" style= "background-color: limegreen" data-sort-by="prezzo">Prezzo</button>
                        <button class="btn-lg" style= "background-color: limegreen" data-sort-by="posizione">Posizione in classifica</button>
                        <button class="btn-lg" style= "background-color: limegreen" data-sort-by="distanza">Alfabetico</button>
                    </div>
                    
                    <c:forEach var='r' items="${restaurantsList}">
                        <div class="container-fluid riquadro_ristorante">
                            <div class="col-md-4" style=" padding-left: 1%; padding-top: 1%; padding-bottom: 1%; ">
                              <img src="data/sfondo_restaurant.jpg" class="img-rounded" alt="<c:out value="${r.name}"/>" style ="max-width: 100%; max-height : 100%; min-width:160px; min-height: 49px;">
                            </div>
                            <a href="Restaurant?restaurantID=<c:out value="${r.id}"/>"> <span style="font-size: 200%; color: royalblue"><b><c:out value="${r.name}"/></b></span> </a>
                            <div class="col-md-8">
                                <span style="color: green;"><b>N. 1 dei ristoranti a Pisellolandia</b></span><br>
                                <div>
                                    <span class="glyphicon glyphicon-star media"></span>
                                    <span class="glyphicon glyphicon-star media"></span>
                                    <span class="glyphicon glyphicon-star media"></span>
                                    <span class="glyphicon glyphicon-star media"></span>
                                    <span class="glyphicon glyphicon-star-empty media"></span>&nbsp;
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
                                    <span class="btn btn-success" style="background-color:limegreen; color: white;">Pizza con le cozze</span>&nbsp; 
                                    <span class="btn btn-success" style="background-color:limegreen; color: white;">Europene</span>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    
                    
                </div>
            </div>
        </div>
        
        <div class="container-fluid">                 
            <ul class="pager">
              <li><a href="#" style="background-color:limegreen; color: black;">Previous</a></li>
              <li><a href="#" style="background-color:limegreen; color: black;">Next</a></li>
            </ul>
        </div>
        <%--  
        <table id ="ristoranti" border="1px">
            <thead>
                <th>Nome</th>
                <th>Indirizzo</th>
                <th>Id</th>
            </thead>
        <c:forEach var='r' items="${restaurantsList}">
                <tr>
                    <td>
                        <c:out value="${r.name}"/>

                    </td>
                    <td>
                        <c:out value="${r.address}"/>
                    </td>
                    <td>
                        <c:out value="${r.id}"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
        
        --%>
            
        <%@include file="footer.html" %>
        
    </body>
</html>

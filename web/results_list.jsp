<!-- Pagina che visualizza i risultati di una ricerca -->

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ristoranti Trovati</title>
        <%@include file="header_head.jsp" %>   
    </head>
    
    <body style=" background-color: gainsboro">
        
       <%@include file="header.jsp" %>
       
       <div class="container-fluid" style=" padding-top: 0px;">
            <div class="row">
                <!-- Form per la ricerca avanzata -->
                <div class="col-sm-3 col-md-3">
                    <%@include file="ricerca.jsp" %>
                </div>
                
                <div class="col-sm-9" id="centro">
                    <div class="container-fluid" style ="padding-top: 1%" >
                        <label> Ordina per : </label>
                        <br>
                        <!-- Bottoni per riordinamento risultati. Ordinamenti possibili: per prezzo, per valutazione, ordine alfabetico -->
                        <form  action = "<c:url value='RestaurantsList'/>" method="POST">
                            <input type="hidden" name="query_id" value ="<c:out value='${query_id}' />">
                            
                            <div class="col-sm-1" style="margin-right: 5%; width: inherit">
                                <button class="btn" style= "background-color: limegreen; font-size: 90%" name = "button" value ="Price">Prezzo</button>
                            </div>
                            <div class="col-sm-1 " style="margin-right: 5%; width: inherit">
                                <button class="btn " style= "background-color: limegreen; font-size: 90%" name = "button" value ="Name">Alfabetico</button>
                            </div>
                            <div class="col-sm-1" style="margin-right: 5%; width: inherit">
                                <button class="btn" style= "background-color: limegreen; font-size: 90%" name ="button" value="Position">Per Valutazione</button>
                            </div>
                        </form>                      
                    </div>
                    
                    <!-- Messaggio visualizzato nel caso in cui non vi siano risulta per la ricerca effettuata -->        
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
                    
                    <!-- Visualizzazione di un ristorante -->
                    <c:forEach var='r' items="${results}"> 
                        <br>
                        <div class="container-fluid riquadro_ristorante">
                            <div class="col-md-4" style=" padding-left: 1%; padding-top: 1%; padding-bottom: 1%; ">
                                <c:choose>
                                    <c:when test="${r.firstPhoto != null}">
                                        <img src="<c:out value='${r.firstPhoto.path}'/>" class="img-rounded" alt="<c:out value="${r.name}"/>" style ="width:280px; height: 160px;">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="data/sfondo_restaurant.jpg" class="img-rounded" alt="<c:out value="${r.name}"/>" style ="width:280px; height: 160px;">
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <a href="<c:url value='Restaurant'><c:param name='restaurantID' value='${r.id}'/></c:url>"> <span style="font-size: 200%; color: royalblue"><b><c:out value="${r.name}"/></b></span> </a>
                            <div class="col-md-8">
                                <span style="color: green;"><b>N. <c:out value="${r.posizione}"/> su <c:out value="${resultsDim}"/> risultati trovati</b></span><br>
                                <div>
                                    <c:forEach var='i' begin='1' end='${Math.round(r.global_review)}' step='1'>
                                        <span class="glyphicon glyphicon-star media"></span>
                                    </c:forEach>
                                    <c:forEach var='i' begin='${Math.round(r.global_review) + 1}' end ='5' step='1'>
                                        <span class="glyphicon glyphicon-star-empty media"></span>
                                    </c:forEach>
                                    &nbsp;
				    <span class="badge" style="margin-top:8px;"><fmt:formatNumber type="number" maxFractionDigits="2" value="${r.global_review}" />&nbsp;<span class="glyphicon glyphicon-star-empty"></span>&nbsp; su <c:out value="${r.review_count}"/> recensioni</span>
                                </div>
                                <br>
                                <div style="margin-top:-10px;">
                                    <a href="<c:url value='Map'><c:param name='id' value='${r.id}'/></c:url>"><span class="glyphicon glyphicon-map-marker"></span>&nbsp; Mappa &nbsp;</a>|&nbsp;
                                    <span>Prezzo: <b><c:if test="${r.min_price != null}"><c:out value="${r.min_price}"/> €</c:if><c:if test="${r.min_price != null && r.max_price != null}"> - </c:if><c:if test="${r.max_price != null}"><c:out value="${r.max_price}"/> €</c:if></b></span><br>
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
        
        <!-- Bottoni per muoversi da una pagina all'altra dei risultati -->
        <div class="container-fluid">  
            <form method="POST" action="<c:url value='RestaurantsList'/>">
                <ul class="pager">
                    <li><input type="hidden" name="query_id" value ="<c:out value='${query_id}' />"></li>
                    <li><input type="hidden" name="page" value ="<c:out value='${page}' />"></li>
                    <li><input type="submit" style="background-color:limegreen; color: black;" name="button" value="Previous"></li>
                    <li><input type="submit" style="background-color:limegreen; color: black;" name="button" value="Next"></li>
                </ul>
            </form>
        </div>
       
                    
        <%@include file="footer.html" %>
        
        <%@include file="js_include.jsp" %>
        
    </body>
</html>

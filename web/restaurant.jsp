
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <!-- This meta tags makes sure accents and other special caracters are displayed correctly -->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <%@include file="header_head.jsp"%>
        <title><c:out value='${restaurant.name}'/></title>
    </head>
    
    <body style=" background-color: gainsboro">
        <%@include file="header.jsp"%>
        
        <div class="container" style="background-color: white; width: 100%">
            <div class="col-md-12" style="padding-top: 20px">
                <span style="font-size: 250%;"><c:out value='${restaurant.name}'/></span>
                <a href="#" data-toggle="tooltip" data-placement="right" title="Inizia a gestire la tua pagina!">Questo è il tuo ristorante?</a>
            </div>
            <div class="col-md-12" style="padding-left: 2%; padding-top: 10px;">
                <div style="">
                    <c:forEach var='i' begin='1' end='${restaurant.global_review}' step='1'>
                        <span class="glyphicon glyphicon-star media"></span>
                    </c:forEach>
                    <c:forEach var='i' begin='${restaurant.global_review + 1}' end ='5' step='1'>
                        <span class="glyphicon glyphicon-star-empty media"></span>
                    </c:forEach>
                        &nbsp;
                    <span class="badge"><c:out value='${restaurant.global_review}'/>&nbsp;<span class="glyphicon glyphicon-star-empty"></span>&nbsp; su <c:out value='${restaurant.review_count}'/> recensioni</span>
                    <span style="color: green; padding-left: 1%; font-size: 135%;"> |&nbsp; &nbsp; &nbsp;N. 1 dei ristoranti in Italia</span>
                </div>
            </div>
            <div class="col-md-12" style="padding-left: 2%; padding-bottom: 10px;">
                <div style="margin-top:10px;">
                    <span class="glyphicon glyphicon-cutlery"></span>&nbsp;
                    <span>Cucina:</span>&nbsp; 
                    <c:forEach var='c' items="${restaurant.cuisines}">
                        <span style="color:limegreen;"><c:out value="${c}"/></span>&nbsp;
                    </c:forEach>
                </div>
            </div>
        </div>
            
        <div class="col-md-8" style="padding-left: 2%; padding-top: 2%">
            <div id="myCarousel" class="carousel slide" data-ride="carousel" >
                <!-- Indicators -->
                <c:choose>
                    <c:when test="${restaurants.photos.size()>0}">
                        <ol class="carousel-indicators">
                            <c:forEach var='i' begin="0" end='${restaurants.photos.size()-1}'>
                                <li data-target="#myCarousel" data-slide-to="<c:out value='${i}'/>" class=""></li>
                            </c:forEach>
                        </ol>
                        <div class="carousel-inner" role="listbox">
                            <!--<div class="item active">
                             <img src="data/sfondo_restaurant.jpg" alt="First slide">
                            </div>-->
                            <c:forEach var='p' items="${restaurant.photos}">
                              <div class="item">
                                <img src="<c:out value='${p.path}'/>" alt="<c:out value='${p.name}'/>">
                              </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <ol class="carousel-indicators">
                            <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                        </ol>
                        <div class="carousel-inner" role="listbox">
                            <div class="item active">
                             <img src="data/sfondo_restaurant.jpg" alt="First slide">
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
                
                
                <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
                  <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                  <span class="sr-only">Previous</span>
                </a>
                <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
                  <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                  <span class="sr-only">Next</span>
                </a>
            </div>
        </div>
        
        <div class="col-md-4" style="padding-left: 2%; padding-top: 2%;">
            <div  style="height:234px; ">
                <div style="background-color: white; border: 1px solid #e3e3e3; border-bottom: 1px solid #dad7c8;margin: 0">
                    <div style="padding: 15px 18px; border-top: 1px solid #F4F3F0; overflow: hidden">
                        <span class="format_address">
                            <span class="street-address"><c:out value='${restaurant.address}'/> </span>, 
                            <span class="locality">
                                <span>39040</span>,
                                <span><c:out value='${restaurant.city}'/></span>,
                            </span>
                            <span class="country-name"><c:out value='${restaurant.state}'/> &nbsp;</span>
                            <span>
                            </span>
                        </span>
                        <a href="#"><span class="glyphicon glyphicon-map-marker"></span>&nbsp; Mappa</a>
                    </div>
                </div>
                <div style="background-color: white; border: 1px solid #e3e3e3; border-bottom: 1px solid #dad7c8;margin: 0">
                    <div style="padding: 15px 18px; border-top: 1px solid #F4F3F0; overflow: hidden">
                        <span class="glyphicon glyphicon-time" style="top: 0;"><b>&nbsp;Orari:</b></span><br>
                        <c:forEach var='o' items="${restaurant.orari}">
                            <span style="padding-left: 3em"><c:out value='${o.giorno}'/> </span>
                            <span style="float: right; padding-right: 5%"><c:out value='${o.apertura}'/> - <c:out value='${o.chiusura}'/> &nbsp; </span>
                            <br>
                        </c:forEach>
                        
                        
                        
                    </div>
                </div>
                <div style="background-color: white; border: 1px solid #e3e3e3; border-bottom: 1px solid #dad7c8;margin: 0">
                    <div style="padding: 15px 18px; border-top: 1px solid #F4F3F0; overflow: hidden">
                        <span>Prezzo: <b><c:out value='${restaurant.min_price}'/> € - <c:out value='${restaurant.max_price}'/> €</b></span>
                        <span>&nbsp;&nbsp;|&nbsp;&nbsp;</span>
                        <a href="<c:out value='${restaurant.url}'/>"><span class="glyphicon glyphicon-globe"></span> Sito Web</a>
                    </div>
                </div>
            </div>
        
        </div>
            
            
            


        <%@include file="footer.html" %>
        <%@include file="js_include.jsp" %>
        
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        <script>
            $(document).ready(function(){
                $('[data-toggle="tooltip"]').tooltip();   
            });
        </script>
    </body>
</html>
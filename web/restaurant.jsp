
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
                    <span class="glyphicon glyphicon-star media"></span>
                    <span class="glyphicon glyphicon-star media"></span>
                    <span class="glyphicon glyphicon-star media"></span>
                    <span class="glyphicon glyphicon-star media"></span>
                    <span class="glyphicon glyphicon-star-empty media"></span> &nbsp;
                    <span class="badge"><c:out value='${restaurant.global_review}'/>&nbsp;<span class="glyphicon glyphicon-star-empty"></span>&nbsp; su <c:out value='${restaurant.review_count}'/> recensioni</span>
                    <span style="color: green; padding-left: 1%; font-size: 135%;"> |&nbsp; &nbsp; &nbsp;N. 1 dei ristoranti in Italia</span>
                </div>
            </div>
            <div class="col-md-12" style="padding-left: 2%; padding-bottom: 10px;">
                <div style="margin-top:10px;">
                    <span class="glyphicon glyphicon-cutlery"></span>&nbsp;
                    <span>Cucina:</span>&nbsp; 
                    <span style="color:limegreen;">Pizza con le cozze</span> 
                    ,&nbsp;
                    <span style="color:limegreen;">Europea</span>
                </div>
            </div>
        </div>
            
        <div class="col-md-8" style="padding-left: 2%; padding-top: 2%">
            <div id="myCarousel" class="carousel slide" data-ride="carousel" >
                <!-- Indicators -->
                <ol class="carousel-indicators">
                  <li data-target="#myCarousel" data-slide-to="0" class=""></li>
                  <li data-target="#myCarousel" data-slide-to="1" class="active"></li>
                  <li data-target="#myCarousel" data-slide-to="2" class=""></li>
                </ol>
                <div class="carousel-inner" role="listbox">
                  <div class="item">
                    <img src="data/sfondo_restaurant.jpg" alt="First slide">
                  </div>
                  <div class="item active">
                   <img src="data/sfondo_restaurant.jpg" alt="First slide">
                  </div>
                  <div class="item">
                    <img  src="data/sfondo_restaurant.jpg" alt="Third slide">
                  </div>
                </div>
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
                            <span class="street-address">Via Taramelli, 14 </span>, 
                            <span class="locality">
                                <span>39040</span>,
                                <span>Trento</span>,
                            </span>
                            <span class="country-name">Italia &nbsp;</span>
                            <span>
                            </span>
                        </span>
                        <a href="#"><span class="glyphicon glyphicon-map-marker"></span>&nbsp; Mappa</a>
                    </div>
                </div>
                <div style="background-color: white; border: 1px solid #e3e3e3; border-bottom: 1px solid #dad7c8;margin: 0">
                    <div style="padding: 15px 18px; border-top: 1px solid #F4F3F0; overflow: hidden">
                        <span class="glyphicon glyphicon-time" style="top: 0;"><b>&nbsp;Orari:</b></span>
                        <span style="">&nbsp;&nbsp;Mar - Dom </span>
                        
                        <span style="float: right; padding-right: 2%">12:00 - 14:30 &nbsp; | &nbsp; 19:30 - 23:00 </span>
                    </div>
                </div>
                <div style="background-color: white; border: 1px solid #e3e3e3; border-bottom: 1px solid #dad7c8;margin: 0">
                    <div style="padding: 15px 18px; border-top: 1px solid #F4F3F0; overflow: hidden">
                        <span>Prezzo: <b><c:out value='${restaurant.min_price}'/> € - <c:out value='${restaurant.max_price}'/> €</b></span>
                        <span>&nbsp;&nbsp;|&nbsp;&nbsp;</span>
                        <a href="#"><span class="glyphicon glyphicon-globe"></span> Sito Web</a>
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
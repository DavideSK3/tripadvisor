

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <!-- This meta tags makes sure accents and other special caracters are displayed correctly -->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><c:out value='${restaurant.name}'/></title>
        
        <%@include file="header_head.jsp"%>
        
    </head>
    
    <body style=" background-color: gainsboro">
        <%@include file="header.jsp"%>
        
        <div class="container" style="background-color: white; width: 100%">
            <div class="col-md-9">
                <div class="col-md-12" style="padding-top: 20px">
                    <span style="font-size: 250%;"><c:out value='${restaurant.name}'/></span>
                    &nbsp;&nbsp;<a href="<c:url value='#'/>" data-toggle="tooltip" data-placement="right" title="Inizia a gestire la tua pagina!">Questo &egrave il tuo ristorante?</a>
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
                        <span class="badge"><fmt:formatNumber type="number" maxFractionDigits="2" value="${restaurant.global_review}" />&nbsp;<span class="glyphicon glyphicon-star-empty"></span>&nbsp; su <c:out value='${restaurant.review_count}'/> recensioni</span>

                        <span style="color: green; padding-left: 1%; font-size: 135%;"> |&nbsp; &nbsp; &nbsp;N. <c:out value='${restaurant.posizione}'/> dei ristoranti in <c:out value='${restaurant.city}'/></span>    

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
            <div class="col-md-3">
                
                <img src="<c:out value='${restaurant.qr_path}'/>">
            </div>
            
        </div>
                    
        <div class="col-md-12">
            <div class="col-md-8" style="padding-left: 2%; padding-top: 2%">
                <div class="row">
                    <div id="myCarousel" class="carousel slide" data-ride="carousel" >
                        <!-- Indicators -->
                        <c:choose>
                            <c:when test="${restaurant.firstPhoto != null}">
                                <ol class="carousel-indicators">
                                    <li data-target="#myCarousel" data-slide-to="0" class ="active"></li>
                                    <c:forEach var='i' begin="1" end='${restaurant.photos.size()}'>
                                        <li data-target="#myCarousel" data-slide-to="<c:out value='${i}'/>"></li>
                                    </c:forEach>
                                </ol>
                                <div class="carousel-inner" role="listbox">
                                    <div class="item active">
                                        <img class="img-responsive center-block" src="<c:out value='${restaurant.firstPhoto.path}'/>" alt="<c:out value='${restaurant.firstPhoto.name}'/>">
                                    </div>

                                    <c:forEach var='p' items="${restaurant.photos}">
                                      <div class="item">
                                        <img class="img-responsive center-block" src="<c:out value='${p.path}'/>" alt="<c:out value='${p.name}'/>">
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
               <div class="row">
                    <div class="jumbotron" style ="padding:2% 1%">
                        <span style ="color: limegreen; font-size: 140%;"><b>Descrizione:</b></span><br>
                        <p style="font-size: 100%; padding:1% 3%"><c:out value='${restaurant.description}'/></p>
                    </div>
                </div>
            </div>

            <div class="col-md-4" style="padding-left: 2%; padding-top: 2%;">
                <div>
                    <div style="background-color: white; border: 1px solid #e3e3e3; border-bottom: 1px solid #dad7c8;margin: 0">
                        <div style="padding: 15px 18px; border-top: 1px solid #F4F3F0; overflow: hidden">
                            <span class="format_address">
                                <span class="street-address"><c:out value='${restaurant.address}'/></span>, 
                                <span class="locality">
                                    <%--<span>39040</span>,--%>
                                    <span><c:out value='${restaurant.city}'/></span>,
                                    <span class="country-name"><c:out value='${restaurant.region}'/>,</span>
                                </span>
                                <span class="country-name"><c:out value='${restaurant.state}'/></span>
                                
                            </span>
                            <a href="<c:url value='Map'><c:param name='id' value='${restaurant.id}'/></c:url>"><span class="glyphicon glyphicon-map-marker"></span>&nbsp; Mappa</a>
                        </div>
                    </div>
                    <div style="background-color: white; border: 1px solid #e3e3e3; border-bottom: 1px solid #dad7c8;margin: 0">
                        <div style="padding: 15px 18px; border-top: 1px solid #F4F3F0; overflow: hidden">
                            <span class="glyphicon glyphicon-time" style="top: 0;"><b>&nbsp;Orari:</b></span><br>
                            <c:forEach var='o' items="${restaurant.orari}">
                                <span style="padding-left: 3em"><c:out value='${o.giorno}'/> </span>
                                <span style="float: right; padding-right: 5%"><c:out value='${o.getAperturaString()}'/> - <c:out value='${o.getChiusuraString()}'/> &nbsp; </span>
                                <br>
                            </c:forEach>



                        </div>
                    </div>
                    <div style="background-color: white; border: 1px solid #e3e3e3; border-bottom: 1px solid #dad7c8;margin: 0">
                        <div style="padding: 15px 18px; border-top: 1px solid #F4F3F0; overflow: hidden">
                            <span>Prezzo: <b><c:out value='${restaurant.min_price}'/> &euro; - <c:out value='${restaurant.max_price}'/> &euro;</b></span>
                            <span>&nbsp;&nbsp;|&nbsp;&nbsp;</span>
                            <a href="http://<c:out value='${restaurant.url}'/>"><span class="glyphicon glyphicon-globe"></span> Sito Web</a>
                        </div>
                    </div>
                    <div style="background-color: white; border: 1px solid #e3e3e3; border-bottom: 1px solid #dad7c8;margin: 0">
                        <div style="padding: 15px 18px; border-top: 1px solid #F4F3F0; overflow: hidden">
                            <h5 style="margin: 0px 0px;padding-bottom: 10px"><b>Dettagli Voto :</b></h5>
                            <span style="color: grey; padding: 10px 10px">Cucina :</span>
                            <div style="padding-left:40% ;">
                                <c:forEach var='i' begin='1' end='${restaurant.food_review}' step='1'>
                                    <span class="glyphicon glyphicon-star media"></span>
                                </c:forEach>
                                <c:forEach var='i' begin='${restaurant.food_review + 1}' end ='5' step='1'>
                                    <span class="glyphicon glyphicon-star-empty media"></span>
                                </c:forEach>
                                &nbsp;
                            </div>
                            <span style="color: grey; padding: 10px 10px">Servizio :</span>
                            <div style="padding-left:40% ;">
                                <c:forEach var='i' begin='1' end='${restaurant.service_review}' step='1'>
                                    <span class="glyphicon glyphicon-star media"></span>
                                </c:forEach>
                                <c:forEach var='i' begin='${restaurant.service_review + 1}' end ='5' step='1'>
                                    <span class="glyphicon glyphicon-star-empty media"></span>
                                </c:forEach>
                                &nbsp;
                            </div>
                            <span style="color: grey; padding: 10px 10px">Rapporto Qualit&agrave / Prezzo :</span>
                            <div style="padding-left:40% ;">
                                <c:forEach var='i' begin='1' end='${restaurant.money_review}' step='1'>
                                    <span class="glyphicon glyphicon-star media"></span>
                                </c:forEach>
                                <c:forEach var='i' begin='${restaurant.money_review + 1}' end ='5' step='1'>
                                    <span class="glyphicon glyphicon-star-empty media"></span>
                                </c:forEach>
                                &nbsp;
                            </div>
                            <span style="color: grey; padding: 10px 10px">Atmosfera :</span>
                            <div style="padding-left:40% ;">
                                <c:forEach var='i' begin='1' end='${restaurant.atmosphere_review}' step='1'>
                                    <span class="glyphicon glyphicon-star media"></span>
                                </c:forEach>
                                <c:forEach var='i' begin='${restaurant.atmosphere_review + 1}' end ='5' step='1'>
                                    <span class="glyphicon glyphicon-star-empty media"></span>
                                </c:forEach>
                                &nbsp;
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
            
        <div class="col-md-10 col-md-offset-1" style="background-color: whitesmoke; margin-top: 20px; padding: 15px 2%;">
            
            <div class="col-md-8" style="padding-left: 0; padding-right: 0;">
                <h3 style="margin: 0 0; padding-bottom: 5%; color: green"><c:out value='${restaurant.review_count}'/> recensioni su questo ristorante</h3>
            </div>
            <div class="col-md-4" style="padding-left: 0; padding-right: 0;">
                <c:if test="${user != null && user.type != 'A' && user.id != restaurant.id_owner}">
                
                    <button type="button" class="btn btn-info " data-toggle="modal" data-target="#myModal" style="background-color: limegreen; border-color: limegreen;  float:right"> Scrivi una recensione</button>
                    <div class="modal fade" id="myModal" role="dialog">
                        <div class="modal-dialog modal-lg">
                            <div class="modal-content" style="background-color: whitesmoke;">
                                <div class="modal-header" style="border-bottom-width: 0;">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h3 class="modal-title"> <b>Scrivi una recensione</b></h3>
                                 </div>
                                <div class="modal-body" style="border-radius: 20px; border-top-width: 0;border-bottom-width: 0;">
                                    <form ENCTYPE='multipart/form-data' method='POST' action='<c:url value='Review'/>'accept-charset="UTF-8">
                                        <input type="hidden" name ="id_restaurant" value ="<c:out value='${restaurant.id}'/>">
                                        <input type ="hidden" name ="return_address" value ="Restaurant?restaurantID=<c:out value='${restaurant.id}'/>">
                                        <input type ="hidden" name ="review" value ="true">
                                        <div class="col-md-3" style="padding-left: 0;">
                                            <span style="font-size: 130%"><b>Inserisci un voto</b></span>
                                        </div>
                                         
                                        <div class="col-md-3">
                                            <div class="rating" id="rating" style="float:right">
                                                <span><input type="radio" name="global" id="str5" value="5" required><label for="str5"></label></span>
                                                <span><input type="radio" name="global" id="str4" value="4" required><label for="str4"></label></span>
                                                <span><input type="radio" name="global" id="str3" value="3" required><label for="str3"></label></span>
                                                <span><input type="radio" name="global" id="str2" value="2" required><label for="str2"></label></span>
                                                <span><input type="radio" name="global" id="str1" value="1" required><label for="str1"></label></span>
                                            </div>
                                        </div>

                                        <br><br>
                                        <span style="font-size: 120%"><b>Titolo della recensione</b></span> <br><br>
                                        <input type="text" name="title" style="width:100%" maxlength="120" placeholder="Riassumi la tua visita o concentrati su un dettaglio interessante" required>
                                        <br><br>
                                        <span style="font-size: 120%"><b>La tua recensione</b></span><br><br>
                                        <textarea name="description" minlenght="50" style="width:100%; min-height: 30%" placeholder="Racconta ai viaggiatori la tua esperienza: come descriveresti il cibo, l'atmosfera, il servizio?" required></textarea>
                                        <br><br>
                                        <span style="font-size: 120%"><b>Ulteriori dettagli sul voto</b></span><br><br>
                                        <div class="voti">
                                            <span style="color: grey; padding: 10px 10px">Cucina :</span>
                                            <input type="number" name="food" min="1" max="5" required>    
                                            &nbsp;
                                            <span style="color: grey; padding: 10px 10px">Servizio :</span>
                                            <input type="number" name="service" min="1" max="5" required>     
                                            &nbsp;
                                            <span style="color: grey; padding: 10px 10px">Rapporto Qualit&agrave / Prezzo :</span>
                                            <input type="number" name="money" min="1" max="5" required> 
                                            &nbsp;
                                            <span style="color: grey; padding: 10px 10px">Atmosfera :</span>
                                            <input type="number" name="atmosphere" min="1" max="5" required>     
                                        </div>
                                        <div style="padding: 15px 10px;">
                                            <span style="color: grey;" >Inserisci una foto: &nbsp; </span>
                                            <input style=" display: initial"  TYPE='file' NAME='img'>
                                            <input type="text" name="photoName" style="width:50%; padding: 5px 10px" maxlength="25" placeholder="Cosa rappresenta questa foto?">
                                        </div>
                                        <input style="float: right" class="btn btn-default" TYPE='submit'>
                                        <button style="float: left" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    </form>
                                </div>
                                <div class="modal-footer" style="border-top-width: 0;">

                                </div>
                            </div>
                        </div>
                    </div>
                    <button type="button" class="btn btn-info " data-toggle="modal" data-target="#myModal2" style="background-color: limegreen; border-color: limegreen;  float:right; margin-right: 5%;"> Aggiungi una foto</button>
                    <div class="modal fade" id="myModal2" role="dialog">
                        <div class="modal-dialog modal-sm">
                            <div class="modal-content">
                                <div class="modal-header" style="border-bottom-width: 0;">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h3 class="modal-title"> <b>Aggiungi una foto</b></h3>
                                </div>
                                <div class="modal-body" style="border-radius: 20px; border-top-width: 0;border-bottom-width: 0;">
                                    <form ENCTYPE='multipart/form-data' method='POST' action='<c:url value='PhotoUpload'/>'>
                                        <input type="hidden" name ="id_restaurant" value ="<c:out value='${restaurant.id}'/>">
                                        <input type ="hidden" name ="return_address" value ="Restaurant?restaurantID=<c:out value='${restaurant.id}'/>">
                                        <input type="hidden" name ="review" value ="false">
                                        <div style="padding-bottom: 20px; ">
                                            <span style="color: grey; padding-bottom: 10px;" >Inserisci una foto: &nbsp; </span>
                                            <input style=" display: initial"  TYPE='file' NAME='img'>
                                            <input type="text" name="photoName" style="width:100%; padding: 5px 10px; margin: 10px 0px;" maxlength="25" placeholder="Cosa rappresenta questa foto?">
                                        </div>
                                        <input style="float: right" class="btn btn-default" TYPE='submit'>
                                        <button style="float: left" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    </form>
                                </div>
                                <div class="modal-footer" style="border-top-width: 0;">

                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
                       
            
            <c:forEach var='rec' items='${restaurant.recensioni}'>
                

                <div class="col-md-12" style="padding: 10px 0px;">
                    <div style="background-color: white; border: 2px solid #e3e3e3; border-bottom: 1px solid #dad7c8;margin: 0">
                        <div style="padding: 15px 18px; border-top: 2px solid #F4F3F0; overflow: hidden">
                            <div class="col-md-2" style="padding: 10px 0px; border-right: 2px solid limegreen;">
                                <span style="color: #428bca;"><i><b><c:out value='${rec.author}'/></b></i></span><br><br>
                                <span>Recensito il <c:out value='${rec.creation}' /></span>
                            </div>
                            <div class="col-md-7" style="padding: 2px 5px;  border-right: 1px solid limegreen; ">
                                <h4 style="margin: 0px 10px; color: green; border-bottom: 1px solid limegreen;"><i>"<c:out value='${rec.title}'/>"</i></h4>
                                
                                <div style="padding: 5px 3%;">
                                    <c:forEach begin='1' end='${rec.global_value}' step='1'>
                                        <span class="glyphicon glyphicon-star"></span>
                                    </c:forEach>
                                    <c:forEach begin='${rec.global_value + 1}' end ='5' step='1'>
                                        <span class="glyphicon glyphicon-star-empty"></span>
                                    </c:forEach>
                                    &nbsp;
                                </div>
                                <p style="padding: 5px 5%; margin:0 0; line-height: 1.7"><c:out value='${rec.description}'/></p>
                                

                            </div>
                            <div class="col-md-3" style="padding: 0px 15px; ">
                                <h5 style="margin: 0px 0px;padding-top: 20px; padding-bottom: 10px">Dettagli Voto :</h5>
                                <span style="color: grey; padding: 10px 10px">Cucina :</span>
                                <div style="padding: 5px 10%;">
                                    <c:forEach var='i' begin='1' end='${rec.food}' step='1'>
                                        <span class="glyphicon glyphicon-star"></span>
                                    </c:forEach>
                                    <c:forEach var='i' begin='${rec.food + 1}' end ='5' step='1'>
                                        <span class="glyphicon glyphicon-star-empty"></span>
                                    </c:forEach>
                                    &nbsp;
                                </div>
                                <span style="color: grey; padding: 10px 10px">Servizio :</span>
                                <div style="padding: 5px 10%;">
                                    <c:forEach var='i' begin='1' end='${rec.service}' step='1'>
                                        <span class="glyphicon glyphicon-star"></span>
                                    </c:forEach>
                                    <c:forEach var='i' begin='${rec.service + 1}' end ='5' step='1'>
                                        <span class="glyphicon glyphicon-star-empty"></span>
                                    </c:forEach>
                                    &nbsp;
                                </div>
                                <span style="color: grey; padding: 10px 10px">Rapporto Qualit&agrave / Prezzo :</span>
                                <div style="padding: 5px 10%;">
                                    <c:forEach var='i' begin='1' end='${rec.value_for_money}' step='1'>
                                        <span class="glyphicon glyphicon-star"></span>
                                    </c:forEach>
                                    <c:forEach var='i' begin='${rec.value_for_money + 1}' end ='5' step='1'>
                                        <span class="glyphicon glyphicon-star-empty"></span>
                                    </c:forEach>
                                    &nbsp;
                                </div>

                                <span style="color: grey; padding: 10px 10px">Atmosfera :</span>
                                <div style="padding: 5px 10%;">
                                    <c:forEach var='i' begin='1' end='${rec.atmosphere}' step='1'>
                                        <span class="glyphicon glyphicon-star"></span>
                                    </c:forEach>
                                    <c:forEach var='i' begin='${rec.atmosphere + 1}' end ='5' step='1'>
                                        <span class="glyphicon glyphicon-star-empty"></span>
                                    </c:forEach>
                                    &nbsp;
                                </div>


                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
                                    
        </div>
        
        
        
        <%@include file="footer.html" %>
        
        <%@include file="js_include.jsp" %>
        
        <script type="text/javascript">
            $(document).ready(function(){
            //  Check Radio-box
                $("#rating input:radio").attr("checked", false);
                $('#rating input').click(function () {
                    $("#rating span").removeClass('checked');
                    $(this).parent().addClass('checked');
                });
        });
        </script>
        
        <script>
            $(document).ready(function(){
                $('[data-toggle="tooltip"]').tooltip();   
            });
        </script>
        
        
        
        
        
        
        
        
        
        
    </body>
</html>
<!-- Pagina che contiene tutti i form necessari per modificare i dati di un ristorante -->

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Modifica Ristorante</title>
        <%@include file="header_head.jsp" %>
        <style> 
            input, label {
                margin: 5px 0;
            }
        </style>
    </head>
        
    <body>
        <div class="container">
            <div class="row">
                <div class="col-sm-6 col-md-4 col-md-offset-4">
                    <h1 class="text-center login-title">Modifica Ristorante</h1>
                    <div class="account-wall">
                        <a class="navbar-brand header" href="<c:url value='/'/>">
                            <img class="profile-img" src="data/TripAdvisor_logo.png" alt="logo" style="max-width:360px; max-height: 90px;">
                        </a><br><br><br><br><br>

                        <!-- Modifica i dettagli relativi al ristorante, contenuti nella tabella restaurant del db -->
                        <form class="form-edit-restaurant" action="<c:url value='ManageRestaurant'/>" method="POST">
                            <input type="text" class="form-control" name="name" placeholder="name" value="<c:out value='${requestScope.results.name}'/>">
                            <input type="text" class="form-control" name="description" placeholder="description" value="<c:out value='${requestScope.results.description}'/>">
                            <input type="text" class="form-control" name="url" placeholder="url" value="<c:out value='${requestScope.results.url}'/>">
                            <input type="text" class="form-control" name="address" placeholder="address" value="<c:out value='${requestScope.results.address}'/>">
                            <input type="text" class="form-control" name="min_price" placeholder="min_price" value="<c:out value='${requestScope.results.min_price}'/>">
                            <input type="text" class="form-control" name="max_price" placeholder="max_price" value="<c:out value='${requestScope.results.max_price}'/>">
                            <input type="hidden" class="form-control" name="restaurantID" value="<c:out value='${requestScope.results.id}'/>">

                            <button class="btn btn-lg btn-primary btn-block" name="modifica" value="modifica_ristorante" type="submit"> Modifica</button>
                            <a href="<c:url value='Profile'/>" class="pull-right need-help">Annulla</a><span class="clearfix"></span>
                        </form> 
                        <br>
                        <button class="btn btn-lg btn-primary btn-block" data-toggle="modal" data-target="#newFoto"> Aggiungi Foto</button>
                        <br>
                        
                        <!-- Form per inserire un nuovo orario all' interno della tabella Orari e relativo al ristorante in questione -->
                        <form class="form-edit-restaurant" action="<c:url value='ManageRestaurant'/>" method="POST">    
                            <h3 class="text-center login-title">Inserisci Orario</h3>
                            Giorno:<br><br>
                            <select name="giorno">
                                <option value="1">Lunedì</option>
                                <option value="2">Martedì</option>
                                <option value="3">Mercoledì</option>
                                <option value="4">Giovedì</option>
                                <option value="5">Venerdì</option>
                                <option value="6">Sabato</option>
                                <option value="7">Domenica</option>
                            </select><br><br>
                            Apertura :<input type="number" min="00" max="23" class="form-control" name="ora_apertura" placeholder="Ore" required >
                            <input type="number" min="00" max="59" class="form-control" name="minuti_apertura" placeholder="Minuti" required><br>
                            Chiusura :<input type="number" min="00" max="24" class="form-control" name="ora_chiusura" placeholder="Ore" required>
                            <input type="number" min="00" max="59" class="form-control" name="minuti_chiusura" placeholder="Minuti" required><br>
                            <input type="hidden" class="form-control" name="restaurantID" value="<c:out value='${requestScope.results.id}'/>">
                            
                            <button class="btn btn-lg btn-primary btn-block" name="modifica" value ="inserisci_orario" type="submit"> Aggiungi Orario</button>
                        </form>
                        <br>
                        <h3 class="text-center login-title">Rimuovi Orari</h3><br>
                        <c:if test="${results.orari == null || results.orari.size() == 0}">
                            <p>Nessun orario impostato per questo ristorante</p>
                        </c:if>
                        <c:forEach var="o" items="${results.orari}">
                            
                            <!-- Form per rimuovere un orario dalla tabella Orari  -->
                            <form class="form-edit-restaurant" method="POST" action ="<c:url value='ManageRestaurant'/>">
                                
                                <input type="hidden" name="apertura" value="<c:out value='${o.getAperturaString()}'/>">
                                <input type="hidden" name="chiusura" value="<c:out value='${o.getChiusuraString()}'/>">
                                <input type="hidden" name="giorno" value="<c:out value='${o.getGiornoAsInt()}'/>">
                                <input type="hidden" name="restaurantID" value="<c:out value='${results.id}'/>">
                                <span>
                                    <c:out value='${o.giorno}'/>: <c:out value='${o.getAperturaString()}'/> - <c:out value='${o.getChiusuraString()}'/> <button class="btn btn-primary" style="float: right" name="modifica" value ="rimuovi_orario" type="submit">Rimuovi</button>
                                </span>
                                
                            </form>
                            <br>
                        </c:forEach>
                        
                        <br><h3 class="text-center login-title">Modifica Cucine</h3><br>
                        
                        <!-- Form per inserire un nuovo tipo di cucina all' interno della tabella Cucina  -->
                        <form class="form-edit-restaurant" action="<c:url value='ManageRestaurant'/>" method="POST">    
                            <input type="hidden" class="form-control" name="restaurantID" value="<c:out value='${requestScope.results.id}'/>">
                            <span>
                                <select name="cucina">
                                    <c:forEach var='i' begin="1" end="${cuisines.size()}">
                                        <option value="<c:out value='${i}'/>"><c:out value='${cuisines.get(i-1)}'/></option>
                                    </c:forEach>
                                </select>

                                <button class="btn btn-primary" style="float: right; width:75px" name="modifica" value ="inserisci_cucina" type="submit">Aggiungi</button>
                            </span>

                        </form>
                            

                        <c:forEach var="c" items="${results.cuisines}">
                            <br>
                            <!-- Form per rimuovere un tipo di cucina della tabella Cucina  -->
                            <form class="form-edit-restaurant" method="POST" action ="<c:url value='ManageRestaurant'/>">
                                <input type="hidden" name="restaurantID" value="<c:out value='${results.id}'/>">
                                <input type="hidden" name="cucina" value="<c:out value='${c}'/>">
                                <span>
                                    <c:out value='${c}'/>
                                    <button class="btn btn-primary" style="float: right; width:75px" name="modifica" value ="rimuovi_cucina" type="submit">Rimuovi</button>
                                </span>
                            </form>
                        </c:forEach>

                        <br><a href="<c:url value='Profile'/>" class="pull-right need-help">Annulla</a><span class="clearfix"></span>
                        <br><br>
                        <div class="modal fade" id="newFoto" role="dialog">
                            <div class="modal-dialog modal-sm">
                                <div class="modal-content">
                                    <div class="modal-header" style="border-bottom-width: 0;">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h3 class="modal-title"> <b>Aggiungi una foto</b></h3>
                                    </div>
                                    <div class="modal-body" style="border-radius: 20px; border-top-width: 0;border-bottom-width: 0;">
                                        
                                        <!-- Form per inserire una nuova foto all' interno della tabella Photos  -->
                                        <form ENCTYPE='multipart/form-data' method='POST' action='<c:url value='PhotoUpload'/>'>
                                            <input type="hidden" name ="id_restaurant" value ="<c:out value='${results.id}'/>">
                                            <input type ="hidden" name ="return_address" value ="<c:url value='Restaurant'><c:param value='${results.id}' name='restaurantID'/></c:url>">
                                            <input type="hidden" name ="review" value ="false">
                                            <div style="padding-bottom: 20px; ">
                                                <span style="color: grey; padding-bottom: 10px;" >Inserisci una foto: &nbsp; </span>
                                                <input style=" display: initial"  TYPE='file' NAME='img'>
                                                <input type="text" name="photoName" style="width:100%; padding: 5px 10px; margin: 10px 0px;" maxlength="25" placeholder="Cosa rappresenta questa foto?">
                                            </div>
                                            <input style="float: right" class="btn btn-default" TYPE='submit'>
                                        </form>
                                    </div>
                                    <div class="modal-footer" style="border-top-width: 0;">
                                        <button style="float: left" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="js_include.jsp" %>
        <%@include file="footer.html" %>
    </body>
</html>
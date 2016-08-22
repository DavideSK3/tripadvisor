
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Modifica Ristorante</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="css/bootstrap.css">
        
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
                        <img class="profile-img" src="data/TripAdvisor_logo.png" alt="logo" style="max-width:360px; max-height: 90px;">
                        <br>

                        <form class="form-edit-restaurant" action="ManageRestaurant" method="POST">
                            <input type="text" class="form-control" name="description" value="${requestScope.results.description}">
                            <input type="text" class="form-control" name="url" value="${requestScope.results.url}">
                            <input type="text" class="form-control" name="address" value="${requestScope.results.address}">
                            <input type="text" class="form-control" name="min_price" value="${requestScope.results.min_price}">
                            <input type="text" class="form-control" name="max_price" value="${requestScope.results.max_price}">
                            <input type="hidden" class="form-control" name="id" value="${requestScope.results.id}">

                            <button class="btn btn-lg btn-primary btn-block" name="modifica" value="modifica_ristorante" type="submit"> Modifica</button>
                            <a href="Profile" class="pull-right need-help">Annulla</a><span class="clearfix"></span>
                        </form> 
                        <br>
                        <button type="button" class="btn btn-info " data-toggle="modal" data-target="#newFoto" style="background-color: limegreen; border-color: limegreen;width: 100%; padding: 3% 2%; font-size: 120%"> Aggiungi una foto</button>
                        <br>
                        
                            <!--<button class="btn btn-lg btn-primary btn-danger" type="submit" style="width:150px" name="cancella_orario" value="segnala"> Cancella Orario</button>-->
                        <form class="form-edit-restaurant" action="ManageRestaurant" method="POST">    
                            <h3 class="text-center login-title">Inserisci Orario</h3>
                            Giorno:<br><br>
                            <select name="giorno">
                                <option value="0">Lunedì</option>
                                <option value="1">Martedì</option>
                                <option value="2">Mercoledì</option>
                                <option value="3">Giovedì</option>
                                <option value="4">Venerdì</option>
                                <option value="5">Sabato</option>
                                <option value="6">Domenica</option>
                            </select><br><br>
                            Apertura :<input type="number" min="00" max="23" class="form-control" name="ora_apertura" placeholder="Ore" required >
                            <input type="number" min="00" max="59" class="form-control" name="minuti_apertura" placeholder="Minuti required"><br>
                            Chiusura :<input type="number" min="00" max="24" class="form-control" name="ora_chiusura" placeholder="Ore" required>
                            <input type="number" min="00" max="59" class="form-control" name="minuti_chiusura" placeholder="Minuti" required><br>
                            <input type="hidden" class="form-control" name="id" value="${requestScope.results.id}">

                            <button class="btn btn-lg btn-primary btn-block" name="modifica" value ="inserisci_orario" type="submit"> Aggiungi Orario</button>

                            <a href="Profile" class="pull-right need-help">Annulla</a><span class="clearfix"></span>
                        </form>

                        
                        <div class="modal fade" id="newFoto" role="dialog">
                            <div class="modal-dialog modal-sm">
                                <div class="modal-content">
                                    <div class="modal-header" style="border-bottom-width: 0;">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h3 class="modal-title"> <b>Aggiungi una foto</b></h3>
                                    </div>
                                    <div class="modal-body" style="border-radius: 20px; border-top-width: 0;border-bottom-width: 0;">
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
                                            <button style="float: left" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                        </form>
                                    </div>
                                    <div class="modal-footer" style="border-top-width: 0;">

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="js_include.jsp" %>
    </body>
</html>
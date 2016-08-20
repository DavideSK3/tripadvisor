


    <div class="sidebar-nav">
        <nav class="navbar navbar-default" role="navigation" style="border-radius: 15px; padding: 1.5%; margin: 3%;">
            <!--<div class = "collapse navbar-collapse" id = "example-navbar-collapse">-->
                <form action="<c:url value='RestaurantsList'/>" method="POST">
                    <ul class="nav navbar-nav">
                        <li><input type="text" class="form-control" placeholder="Dove vai?" name="place" id ="advanced_search_place" value="<c:out value='${requestScope.place}'/>"></li>
                        <li><input type="text" class="form-control" placeholder="Ricerca ristorante" name="r_query" id ="advanced_search_name" value="<c:out value='${requestScope.r_query}'/>"></li>


                        <li><label style="padding-left: 5%; padding-top: 2%; font-size: 150%">Ricerca avanzata:</label>
                        <br>
                        <li><label style="padding-left: 4%;">Range di prezzo:</label>
                            <label style="padding-left: 5%;"> Min : &nbsp;<input type="number" class="form-control" id= "min_max"   style="max-width: 30%" name ="min_price" value ="<c:out value='${requestScope.min_price}'/>"/></label>
                            <label style="padding-left: 5%;"> Max : &nbsp;<input type="number" class="form-control" id= "min_max"  style="max-width: 30%" name ="max_price"  value ="<c:out value='${requestScope.max_price}'/>"/></label>
                        </li>
                        <li><label style="padding-left: 4%; ">Categorie:</label><br>
                            <div style="padding-left: 10%">
                                <c:forEach var ='c' items ='${cuisines}'>
                                    <input type="checkbox" name= "cusines" value="<c:out value='${c}'/>" <c:if test='${requestScope[c] == true}'>checked</c:if>>
                                    <c:out value='${c}'/><br>
                                </c:forEach>
                            </div>
                        </li>
                        <li><label style="padding-left: 4%;">Valutazione:</label><br>
                            <div style="padding-left: 10%">  
                                <input type="checkbox" name="valutazione" value="5" <c:if test='${requestScope.v5 == true}'>checked</c:if>> 5<span class="glyphicon glyphicon-star-empty"></span>
                                <input type="checkbox" name="valutazione" value="4" <c:if test='${requestScope.v4 == true}'>checked</c:if>> 4<span class="glyphicon glyphicon-star-empty"></span>
                                <input type="checkbox" name="valutazione" value="3" <c:if test='${requestScope.v3 == true}'>checked</c:if>> 3<span class="glyphicon glyphicon-star-empty"></span>
                                <input type="checkbox" name="valutazione" value="2" <c:if test='${requestScope.v2 == true}'>checked</c:if>> 2<span class="glyphicon glyphicon-star-empty"></span>
                                <input type="checkbox" name="valutazione" value="1" <c:if test='${requestScope.v1 == true}'>checked</c:if>> 1<span class="glyphicon glyphicon-star-empty"></span>
                            </div>
                        </li>
                        <li id ="gl_distance_form" onclick="getLocation()" >
                            <label style="padding-left: 4%;" id ="error"></label><br>
                            <label style="padding-left: 4%;">Distanza massima: (in metri)</label>
                            <p id="geolocation_error" style="text-align: center; color: red; font-weight: bold"></p>
                            <label style="padding-left: 5%;"> Max : &nbsp;<input type="number" class="form-control" id= "min_max" onclick="getLocation()"  style="max-width: 30%" name ="distance" value = "<c:out value='${requestScope.distance}'/>"/></label>
                        </li>
                        <li>
                            <input type="submit" name="button" value="Search">
                        </li>
                        <input type="hidden" name ="longitude" id ="long">
                        <input type="hidden" name ="latitude" id ="lat">

                    </ul>
                </form>
            <!--</div>-->
        </nav>

        <script type="text/javascript">
            function showPosition(position) {
                    console.log(position.coords.latitude + '   ' + position.coords.longitude);

                    document.getElementById("lat").value = position.coords.latitude;
                    document.getElementById("long").value = position.coords.longitude; 
                }

                function error(err) {
                  console.warn('ERROR(' + err.code + '): ' + err.message);
                  document.getElementById("geolocation_error").textContent = 'No position available.';
                }

                function getLocation() {

                    if (navigator.geolocation) {
                        navigator.geolocation.getCurrentPosition(showPosition, error);
                    } else {
                        alert("Sorry, no position available.");
                        document.getElementById("geolocation_error").textContent = "No position available.";
                        document.getElementById("min_max").disabled = true;
                    }
                }

        </script>

    </div>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <style>
       #map {
        width: 100%;
        height: 600px;
      }
    </style>
    <link rel="icon" href="favicon.png" type="image/png" />
    <title>Mapp</title>
  </head>
  <body>
    <div id="map"></div>
    <script>
      function initMap() {
        var mapDiv = document.getElementById('map');
        var map = new google.maps.Map(mapDiv, {
            center: {lat: <c:out value='${latitude}'/>, lng: <c:out value='${longitude}'/>},
            zoom: 15
        });
        var marker = new google.maps.Marker({position:(new google.maps.LatLng(<c:out value='${latitude}'/>,<c:out value='${longitude}'/>))});
            marker.setMap(map);
      }
      
    </script>
    <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyClHNpB3sEhJqzwms8IVInpQyB5zPP3b10&callback=initMap"></script>
    
  </body>
</html>
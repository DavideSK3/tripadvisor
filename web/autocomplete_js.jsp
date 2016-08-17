<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
$(function () {
    
    $("#search_name").autocomplete({
        source: function (request, response) {
            $.ajax({
                contentType: "application/json",
                type: "GET",
                url: "http://<c:out value='${initParam.webAddress}'/>/TripAdvisor/services/autocomplete/restaurants/" + request.term,
                data: {
                    term: request.term
                },
                success: function (data, textStatus, jqXHR) {
                    response(data);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    jqXHR;
                }
            });
        },
        minLength: 4,
        delay: 400
    });
    
    $("#advanced_search_name").autocomplete({
        source: function (request, response) {
            $.ajax({
                contentType: "application/json",
                type: "GET",
                url: "http://<c:out value='${initParam.webAddress}'/>/TripAdvisor/services/autocomplete/restaurants/" + request.term,
                data: {
                    term: request.term
                },
                success: function (data, textStatus, jqXHR) {
                    response(data);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    jqXHR;
                }
            });
        },
        minLength: 4,
        delay: 400
    });
    
    $("#advanced_search_place").autocomplete({
        source: function (request, response) {
            $.ajax({
                contentType: "application/json",
                type: "GET",
                url: "http://<c:out value='${initParam.webAddress}'/>/TripAdvisor/services/autocomplete/places/" + request.term,
                data: {
                    term: request.term
                },
                success: function (data, textStatus, jqXHR) {
                    response(data);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    jqXHR;
                }
            });
        },
        minLength: 4,
        delay: 400
    });
    
    
    $("#search_place").autocomplete({
        source: function (request, response) {
            $.ajax({
                contentType: "application/json",
                type: "GET",
                url: "http://<c:out value='${initParam.webAddress}'/>/TripAdvisor/services/autocomplete/places/" + request.term,
                data: {
                    term: request.term
                },
                success: function (data, textStatus, jqXHR) {
                    response(data);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    jqXHR;
                }
            });
        },
        minLength: 4,
        delay: 400
    });
    
    
    
});

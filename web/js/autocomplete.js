$(function () {
    
    $("#search_name").autocomplete({
        source: function (request, response) {
            $.ajax({
                contentType: "application/json",
                type: "GET",
                url: "http://localhost:8084/TripAdvisor/services/autocomplete/restaurants/" + request.term,
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
                url: "http://localhost:8084/TripAdvisor/services/autocomplete/restaurants/" + request.term,
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
                url: "http://localhost:8084/TripAdvisor/services/autocomplete/places/" + request.term,
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
                url: "http://localhost:8084/TripAdvisor/services/autocomplete/places/" + request.term,
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

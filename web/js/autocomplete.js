$(function () {
    
    $("#search_name").autocomplete({
        source: function (request, response) {
            $.ajax({
                contentType: "application/json",
                type: "GET",
                url: "http://localhost:8084/TripAdvisor/services/autocomplete/" + request.term,
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
        minLength: 3,
        delay: 400
    });
});

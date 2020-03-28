var stompClient

function connect() {
    var socket = new SockJS('/track');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/user/queue/reply', function (greeting) {
            console.log(greeting.body);
        });
    });
}


function sendName() {
    stompClient.send("/app/track", {}, "elo");
}


$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});
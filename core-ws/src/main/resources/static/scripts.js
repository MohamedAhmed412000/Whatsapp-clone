var stompClient = null;

$(document).ready(function() {
    console.log("Index page is ready");
    $("#send-private").click(function() {
        sendPrivateMessage();
    });
    $("#connect").click(function() {
        connect();
    });
});

function connect() {
    let userId = document.getElementById("user-id").value;

    try {
        // Try to set up WebSocket connection with the handshake
        var socket = new SockJS('http://localhost:8095/ws');

        // Create a new StompClient object with the WebSocket endpoint
        stompClient = Stomp.over(socket);

        // Start the STOMP communications, provide a callback for when the CONNECT frame arrives.
        stompClient.connect({'user-id': userId}, function (frame) {
                showInfo(frame);
                console.log(frame);
                stompClient.subscribe(`/users/${userId}/queue/chat`, function (message) {
                    showMessage(JSON.parse(message.body).content);
                });
            },
            function (err){
                showInfo(err);
            }
        );
    } catch (Exception) {
        console.error(Exception);
    }
}
function sendPrivateMessage() {
    let message = document.getElementById("private-message").value;
    let jwtToken = document.getElementById("jwt-token").value;
    console.log("Sending private message '"+message+"' to user with token "+jwtToken);

    stompClient.send(
      "/app/message/" + userId,
      {'Authorization': 'Bearer ' + apiKey },
      JSON.stringify({'messageContent': message})
    );
}

function showMessage(message) {
    $("#messages").append("<tr><td style='font-size: 8px'>" + message + "</td></tr>");
}

function showInfo(message) {
    $("#connected").append("<tr><td>" + message + "</td></tr>");
}



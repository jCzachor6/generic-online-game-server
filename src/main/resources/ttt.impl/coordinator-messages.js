var searchCounter = 0;
var acceptCounter = 20;
var searchInterval;
var acceptInterval
var roomUUID;

function messageHandler() {
    return function (data) {
        var message = JSON.parse(data);
        console.log(message);
        switch (message.type) {
            case 'SEARCHING':
                coordinatorSearchingHandler(message);
                break;
            case 'CANCELED':
                coordinatorCanceledHandler(message);
                break;
            case 'DECLINED':
                coordinatorDeclinedHandler(message);
                break;
            case 'FOUND':
                coordinatorFoundHandler(message);
                break;
            case 'REQUIRE_ACCEPT':
                coordinatorRequireAcceptHandler(message);
                break;
        }
    }
}

function coordinatorSearchMessage() {
    appSocket.emit('SEARCH', {});
}

function coordinatorCancelMessage() {
    appSocket.emit('CANCEL', {});
}

function coordinatorAcceptMessage() {
    appSocket.emit('ACCEPT', {roomUUID: roomUUID});
}

function coordinatorDeclineMessage() {
    appSocket.emit('DECLINE', {roomUUID: roomUUID});
}

function coordinatorRequireAcceptHandler(msg) {
    roomUUID = msg.roomUUID;
    $("#search-content").css("display", "none");
    $("#game-found-content").css("display", "block");
    if (searchInterval) {
        clearInterval(searchInterval);
    }
    acceptCounter = 15;
    acceptInterval = setInterval(function () {
        acceptCounter--;
        $("#accept-timer").text(acceptCounter);
        if (acceptCounter === 0) {
            clearInterval(acceptInterval);
            $("#search-content").css("display", "block");
            $("#game-found-content").css("display", "none");
        }
    }, 1000);
}

function coordinatorSearchingHandler(msg) {
    searchCounter = 0;
    if (searchInterval) {
        clearInterval(searchInterval);
    }
    searchInterval = setInterval(function () {
        searchCounter++;
        $("#searching-timer").text(searchCounter);
    }, 1000);
}

function coordinatorCanceledHandler(msg) {
    clearInterval(searchInterval);
    $("#searching-timer").text('0');
}

function coordinatorDeclinedHandler(msg) {
    if (acceptInterval) {
        clearInterval(acceptInterval);
    }
    $("#search-content").css("display", "block");
    $("#game-found-content").css("display", "none");
}

function coordinatorFoundHandler(msg) {
    roomUUID = msg.roomUUID;
    if (acceptInterval) {
        clearInterval(acceptInterval);
    }
    $("#game-found-content").css("display", "none");
    $("#game-in-content").css("display", "block");
    connectToGameRoom();
}

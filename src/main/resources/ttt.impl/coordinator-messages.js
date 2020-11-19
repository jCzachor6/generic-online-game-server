var searchCounter = 0;
var acceptCounter = 20;
var searchInterval;
var acceptInterval
var foundRoomUUID;

function coordinatorSearchMessage() {
    appSocket.emit('coordinator', {type: 'SEARCH'});
}

function coordinatorCancelMessage() {
    appSocket.emit('coordinator', {type: 'CANCEL'});
}

function coordinatorAcceptMessage() {
    appSocket.emit('coordinator', {type: 'ACCEPT', foundRoomUUID: foundRoomUUID});
}

function coordinatorDeclineMessage() {
    appSocket.emit('coordinator', {type: 'DECLINE', foundRoomUUID: foundRoomUUID});
}

function coordinatorRequireAcceptHandler(msg) {
    foundRoomUUID = msg.foundRoomUUID;
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
    foundRoomUUID = msg.foundRoomUUID;
    if (acceptInterval) {
        clearInterval(acceptInterval);
    }
    $("#game-found-content").css("display", "none");
}

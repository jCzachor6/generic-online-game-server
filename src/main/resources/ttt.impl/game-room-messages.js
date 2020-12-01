function connectToGameRoom() {
    socket = io('http://localhost:9092/ttt/' + roomUUID + '?token=' + user.jwt);
    appSocket = socket.connect();
    appSocket.on('connect', connectHandler());
    appSocket.on('disconnect', disconnectHandler());
    appSocket.on(roomUUID, ticTacToeMessageHandler());
    appSocket.emit('DATA', {});
}

function ticTacToeMessageHandler() {
    return function (data) {
        var message = JSON.parse(data);
        console.log(message.type, message);
        switch (message.type) {
            case 'DATA':
                onDataReceived(message);
                break;
            case 'RESULT':
                onResultReceived(message);
                break;
            case 'RESTART':
                onRestartReceived(message);
                break;
            case 'CLOSE':
                onCloseReceived(message);
                break;
        }
    }
}

function tttSetTile(tileIndex) {
    appSocket.emit('SET_TILE', {tileIndex: tileIndex});
}

function ticTacToeRestartMessage() {
    appSocket.emit('RESTART', {});
}

function ticTacToeLeaveMessage() {
    appSocket.emit('LEAVE', {});
}

function onDataReceived(message) {
    var squares = $(".square");
    for (var i = 0; i < squares.length; i++) {
        if (message.tiles[i] !== 'n' && $(squares[i]).attr("class") === 'square') {
            $(squares[i]).addClass(message.tiles[i])
        }
    }
}

function onRestartReceived(message) {
    var squares = $(".square");
    for (var i = 0; i < squares.length; i++) {
        $(squares[i]).removeClass('x');
        $(squares[i]).removeClass('o');
    }
}

function onCloseReceived(message) {
    var squares = $(".square");
    for (var i = 0; i < squares.length; i++) {
        $(squares[i]).removeClass('x');
        $(squares[i]).removeClass('o');
    }
    $("#game-in-content").css("display", "none");
    $("#search-content").css("display", "block");
    setupWebsocket(user);
}

function onResultReceived(message) {
    onDataReceived(message);
}

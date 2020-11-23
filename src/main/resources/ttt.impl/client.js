var axios;
var user;
var socket;
var appSocket;

const connected = 'CONNECTED';
const disconnected = 'DISCONNECTED';

function init() {
    console.log('init');
}

function logout() {
    if (appSocket != null) {
        appSocket.disconnect();
    }
    user = {username: '---', password: '---', jwt: ''}
    $("#username").text(user.username);
    $("#password").text(user.password);
    $("#status").text(disconnected);
    $(".content").css("display", "none");
    $("#auth-content").css("display", "block");
}

function login() {
    var username = $("#in-user").val();
    var password = $("#in-password").val();
    axios.post('http://localhost:8080/ggs/api/auth/login', {username: username, password: password})
        .then(function (response) {
            user = response.data;
            setupUser(user);
            setupWebsocket(user);
        });
}

function register() {
    var username = $("#in-user").val();
    var password = $("#in-password").val();
    axios.post('http://localhost:8080/ggs/api/auth/register', {username: username, password: password})
        .then(function (response) {
            user = response.data;
            setupUser(user);
            setupWebsocket(user);
        });
}

function anonymous() {
    axios.post('http://localhost:8080/ggs/api/auth/anonymous', {})
        .then(function (response) {
            user = response.data;
            setupUser(user);
            setupWebsocket(user);
        });
}

function setupUser(user) {
    $("#username").text(user.username);
    $("#password").text(user.password);
    $("#status").text(disconnected);
    $("#auth-content").css("display", "none");
    $("#user-content").css("display", "block");
    $("#search-content").css("display", "block");
}

function setupWebsocket(user) {
    if (appSocket != null) {
        appSocket.disconnect();
    }
    socket = io('http://localhost:9092/ttt/coordinator?token=' + user.jwt);
    appSocket = socket.connect();
    appSocket.on('connect', connectHandler());
    appSocket.on('disconnect', disconnectHandler());
    appSocket.on(user.jwt, messageHandler());
}

function connectHandler() {
    return function () {
        $("#status").text(connected);
    }
}

function disconnectHandler() {
    return function () {
        $("#status").text(disconnected);
    }
}

init();

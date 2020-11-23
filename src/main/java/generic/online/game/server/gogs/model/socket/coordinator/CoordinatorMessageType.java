package generic.online.game.server.gogs.model.socket.coordinator;

public enum CoordinatorMessageType {
    //input types
    SEARCH,
    CANCEL,
    ACCEPT,
    DECLINE,

    //output types
    SEARCHING,
    CANCELED,
    REQUIRE_ACCEPT,
    ACCEPTED,
    DECLINED,
    FOUND,
    ERROR
}

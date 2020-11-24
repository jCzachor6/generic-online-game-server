package generic.online.game.server.gogs.model.rooms;

import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.socket.MessageSender;
import lombok.Getter;

import java.util.Set;

@Getter
public class GameRoom<T> {
    protected String roomId;
    protected Set<User> gameUsers;
    protected MessageSender messageSender;
    protected Operations operations;

    public GameRoom(GameRoomInitializerData data) {
        this.roomId = data.getRoomId();
        this.gameUsers = data.getUsers();
        this.messageSender = data.getMessageSender();
        this.operations = data.getOperations();
    }
}

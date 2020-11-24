package generic.online.game.server.gogs.model.rooms;

import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.socket.MessageSender;
import lombok.Value;

import java.util.Set;

@Value
public class GameRoomInitializerData {
    String roomId;
    Set<User> users;
    MessageSender messageSender;
    Operations operations;
}

package generic.online.game.server.gogs.model.rooms;

import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.socket.Messenger;
import lombok.Value;

import java.util.Set;

@Value
public class RoomInitializerData {
    String roomId;
    Set<User> users;
    Messenger messenger;
    Operations operations;
}

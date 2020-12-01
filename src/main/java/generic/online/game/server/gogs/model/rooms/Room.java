package generic.online.game.server.gogs.model.rooms;

import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.socket.Message;
import generic.online.game.server.gogs.model.socket.Messenger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Room<T extends Message> {
    String roomId;
    Set<User> gameUsers;
    Messenger messenger;
    Operations operations;

    public Room(RoomInitializerData data) {
        this.roomId = data.getRoomId();
        this.gameUsers = data.getUsers();
        this.messenger = data.getMessenger();
        this.operations = data.getOperations();
    }
}

package generic.online.game.server.gogs.model.rooms;

import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.impl.rooms.dynamic_room_list.DynamicRoomListOperations;
import generic.online.game.server.gogs.model.socket.Messenger;
import generic.online.game.server.gogs.utils.interfaces.RoomParameters;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Room implements RoomParameters {
    Date createdOn = new Date();
    String roomId;
    Set<User> gameUsers;
    Messenger messenger;
    Operations operations;
    DynamicRoomListOperations dynamicRoomListOperations;

    public Room(RoomInitializerData data) {
        this.roomId = data.getRoomId();
        this.gameUsers = data.getUsers();
        this.messenger = data.getMessenger();
        this.operations = data.getOperations();
        this.dynamicRoomListOperations = data.getDynamicRoomListOperations();
    }

    public void internalRooms(Map<String, Room> map) {
    }

    public void handlers(RoomContext ctx) {
    }
}

package generic.online.game.server.gogs.model.rooms;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import generic.online.game.server.gogs.impl.rooms.dynamic_room_list.DynamicRoomListOperations;
import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.model.socket.Messenger;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class RoomInitializerData {
    private final Map<String, SocketIOClient> clientsMap;
    private final String roomId;
    private final Set<User> users;
    private final Messenger messenger;
    private final List<Timer> roomTimers;
    private Operations operations;
    private DynamicRoomListOperations dynamicRoomListOperations;

    public RoomInitializerData(String roomId, Set<User> users, Messenger messenger) {
        this.clientsMap = new ConcurrentHashMap<>(users.size());
        this.roomId = roomId;
        this.users = users;
        this.messenger = messenger == null ? new Messenger(clientsMap) : messenger;
        this.roomTimers = new ArrayList<>();
    }

    public RoomInitializerData setOperations(String namespace, SocketIOServer server, List<Room> rooms) {
        if (operations == null) {
            this.operations = OperationsInitializer.initialize(this, namespace, server, rooms);
        }
        return this;
    }

    public RoomInitializerData setOperations(Operations operations) {
        this.operations = operations;
        return this;
    }

    public void setRoomListOperations(DynamicRoomListOperations roomListOperations) {
        this.dynamicRoomListOperations = roomListOperations;
    }
}

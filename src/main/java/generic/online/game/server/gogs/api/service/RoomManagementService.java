package generic.online.game.server.gogs.api.service;

import com.corundumstudio.socketio.ClientOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.auth.jwt.JwtAuthenticationFilter;
import generic.online.game.server.gogs.model.rooms.Operations;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomAnnotationsScanner;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.model.socket.Message;
import generic.online.game.server.gogs.model.socket.Messenger;
import generic.online.game.server.gogs.utils.RoomInitializer;
import generic.online.game.server.gogs.utils.settings.SocketSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomManagementService {
    private final List<Room<? extends Message>> rooms = new ArrayList<>();
    private final SocketIOServer server;
    private final SocketSettings socketSettings;
    private final JwtAuthenticationFilter authenticationFilter;

    public Room<? extends Message> addRoom(String roomId,
                                           Set<User> users,
                                           RoomInitializer roomInitializer,
                                           Object additionalData) {
        Map<String, SocketIOClient> clientsMap = new ConcurrentHashMap<>(users.size());
        List<Timer> gameRoomTimers = new ArrayList<>();
        RoomInitializerData data = new RoomInitializerData(
                roomId, users, new Messenger(clientsMap),
                setupOperations(roomId, gameRoomTimers, clientsMap)
        );
        Room<? extends Message> room = roomInitializer.initialize(data, additionalData);
        SocketIONamespace namespace = server.addNamespace(socketSettings.getNamespace() + "/" + roomId);
        new RoomAnnotationsScanner(namespace)
                .forGameRoom(room)
                .setOnEventListeners()
                .setTickRateListener(gameRoomTimers)
                .setUserConnectDisconnectListener(clientsMap, authenticationFilter);
        rooms.add(room);
        return room;
    }

    public void removeRoom(String roomId) {
        rooms.stream().filter(r -> r.getRoomId().equals(roomId)).findFirst().ifPresent((room) -> {
            room.getOperations().closeRoom();
        });
    }

    public void removeRoom(String roomId, int seconds) {
        rooms.stream().filter(r -> r.getRoomId().equals(roomId)).findFirst().ifPresent((room) -> {
            room.getOperations().closeRoomAfterTime(seconds);
        });
    }

    public List<Room<? extends Message>> getRooms() {
        return new ArrayList<>(rooms);
    }

    public Set<User> getUsers() {
        return rooms.stream().flatMap(r -> r.getGameUsers().stream()).collect(Collectors.toUnmodifiableSet());
    }

    private Operations setupOperations(String roomId, List<Timer> timers, Map<String, SocketIOClient> clientsMap) {
        return new Operations() {
            @Override
            public void closeRoom() {
                timers.forEach(Timer::cancel);
                clientsMap.values().forEach(ClientOperations::disconnect);
                server.removeNamespace(socketSettings.getNamespace() + "/" + roomId);
                rooms.remove(
                        rooms.stream().filter(r -> r.getRoomId().equals(roomId)).findFirst().orElse(null)
                );
            }

            @Override
            public Set<User> connectedUsers() {
                return clientsMap.values().stream().map(u -> (User) u.get("user")).collect(Collectors.toSet());
            }
        };
    }
}

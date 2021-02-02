package generic.online.game.server.gogs.api.service;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import generic.online.game.server.gogs.GogsConfig;
import generic.online.game.server.gogs.api.auth.jwt.JwtAuthenticationFilter;
import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.model.rooms.AnnotationMethodsParams;
import generic.online.game.server.gogs.model.rooms.AnnotationsScannerService;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.RoomInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomManagementService {
    private final List<Room> rooms = new ArrayList<>();
    private final AnnotationsScannerService annotationsService;

    private final SocketIOServer server;
    private final GogsConfig gogsConfig;

    public Room addRoom(String roomId, Set<User> users, RoomInitializer roomInitializer, Object additionalData) {
        String namespace = gogsConfig.wsServerNamespace;
        RoomInitializerData data = new RoomInitializerData(roomId, users, null)
                .setOperations(namespace, server, rooms);
        Room room = roomInitializer.initialize(data, additionalData);
        SocketIONamespace IOnamespace = server.addNamespace(namespace + "/" + roomId);
        annotationsService.validateRoomParameters(room);
        setupListeners(new AnnotationMethodsParams(data, IOnamespace, room));
        rooms.add(room);
        return room;
    }

    private void setupListeners(AnnotationMethodsParams mp) {
        Room room = mp.getRoom();
        room.handlers(mp.getContext());
        annotationsService.setOnMessageListeners(mp);
        annotationsService.setOnTickListeners(mp);

        Map<String, Room> innerRooms = new HashMap<>();
        room.internalRooms(innerRooms);
        innerRooms.forEach((key, innerRoom) -> {
            String eventPrefix = key + "-" + mp.getEventPrefix();
            annotationsService.validateRoomParameters(innerRoom);
            AnnotationMethodsParams ip = new AnnotationMethodsParams(mp, innerRoom, eventPrefix);
            setupListeners(ip);
        });
        annotationsService.setUserConnectListener(mp);
        annotationsService.setUserDisconnectListener(mp);
    }

    public void removeRoom(String roomId) {
        rooms.stream().filter(r -> r.getRoomId().equals(roomId)).findFirst().ifPresent((room) -> room.getOperations().closeRoom());
    }

    public void removeRoom(String roomId, int seconds) {
        rooms.stream().filter(r -> r.getRoomId().equals(roomId)).findFirst().ifPresent((room) -> room.getOperations().closeRoomAfterTime(seconds));
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public Set<User> getUsers() {
        return rooms.stream()
                .flatMap(r -> r.getGameUsers().stream())
                .collect(Collectors.toSet());
    }
}

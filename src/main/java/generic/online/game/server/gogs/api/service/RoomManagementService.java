package generic.online.game.server.gogs.api.service;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import generic.online.game.server.gogs.GogsConfig;
import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.model.rooms.AnnotationMethodsParams;
import generic.online.game.server.gogs.model.rooms.AnnotationsScannerService;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.RoomInitializer;
import generic.online.game.server.gogs.utils.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomManagementService {
    private final List<Room> rooms = new ArrayList<>();

    private final SocketIOServer server;
    private final GogsConfig gogsConfig;
    private final AnnotationsScannerService annotationsService;

    public Room addRoom(String roomId, Set<User> users, RoomInitializer roomInitializer, Object additionalData) {
        String namespace = gogsConfig.wsServerNamespace;
        RoomInitializerData data = new RoomInitializerData(roomId, users, null)
                .setOperations(namespace, server, rooms);
        Room room = roomInitializer.initialize(data, additionalData);
        SocketIONamespace IOnamespace = server.addNamespace(namespace + "/" + roomId);
        RoomParameters parameters = annotationsService.getRoomParameters(room);
        setupListeners(new AnnotationMethodsParams(data, IOnamespace, room, parameters));
        rooms.add(room);
        return room;
    }

    private void setupListeners(AnnotationMethodsParams mp) {
        Room room = mp.getRoom();
        HashMap<Class<? extends Annotation>, List<Method>> annotated =
                annotationsService.gogsAnnotatedMethods(room.getClass());
        annotationsService.setOnMessageListeners(mp.withMethods(annotated.get(OnMessage.class)));
        annotationsService.setOnTickListeners(mp.withMethods(annotated.get(OnTick.class)));
        List<Method> onConnect = annotated.get(OnConnect.class);
        if (onConnect.size() == 1) {
            mp.getOnConnect().put(mp.getRoom(), onConnect.get(0));
        }
        List<Method> onDisconnect = annotated.get(OnDisconnect.class);
        if (onDisconnect.size() == 1) {
            mp.getOnDisconnect().put(mp.getRoom(), onDisconnect.get(0));
        }

        try {
            Map<String, Room> innerRooms = annotationsService.gogsInnerRooms(room);
            innerRooms.forEach((key, innerRoom) -> {
                RoomParameters parameters = annotationsService.getRoomParameters(innerRoom);
                String eventPrefix = key + "-" + mp.getEventPrefix();
                AnnotationMethodsParams ip = new AnnotationMethodsParams(mp, innerRoom, eventPrefix, parameters);
                setupListeners(ip);
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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

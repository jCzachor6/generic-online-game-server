package generic.online.game.server.gogs.api.service;

import com.corundumstudio.socketio.ClientOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.rooms.*;
import generic.online.game.server.gogs.utils.RoomInitializer;
import generic.online.game.server.gogs.utils.annotations.*;
import generic.online.game.server.gogs.utils.settings.SocketSettings;
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
    private final SocketSettings socketSettings;
    private final AnnotationsScannerService annotationsService;

    public void addRoom(String roomId, Set<User> users, RoomInitializer roomInitializer, Object additionalData) {
        RoomInitializerData data = new RoomInitializerData(roomId, users, null)
                .setOperations(socketSettings.getNamespace(), server, rooms);
        Room room = roomInitializer.initialize(data, additionalData);
        SocketIONamespace namespace = server.addNamespace(socketSettings.getNamespace() + "/" + roomId);
        RoomParameters parameters = annotationsService.getRoomParameters(room);

        AnnotationMethodsParams methodsParams = new AnnotationMethodsParams(data, namespace, room, parameters);
        setupListeners(methodsParams);
        annotationsService.setUserConnectListener(methodsParams);
        annotationsService.setUserDisconnectListener(methodsParams);
        rooms.add(room);
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
        if (onConnect.size() == 1) {
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

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public Set<User> getUsers() {
        return rooms.stream()
                .flatMap(r -> r.getGameUsers().stream())
                .collect(Collectors.toUnmodifiableSet());
    }
}
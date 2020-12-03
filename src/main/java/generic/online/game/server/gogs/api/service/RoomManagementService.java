package generic.online.game.server.gogs.api.service;

import com.corundumstudio.socketio.ClientOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.auth.jwt.JwtAuthenticationFilter;
import generic.online.game.server.gogs.model.rooms.*;
import generic.online.game.server.gogs.model.socket.Message;
import generic.online.game.server.gogs.model.socket.Messenger;
import generic.online.game.server.gogs.utils.RoomInitializer;
import generic.online.game.server.gogs.utils.annotations.OnConnect;
import generic.online.game.server.gogs.utils.annotations.OnDisconnect;
import generic.online.game.server.gogs.utils.annotations.OnMessage;
import generic.online.game.server.gogs.utils.annotations.OnTick;
import generic.online.game.server.gogs.utils.settings.SocketSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
    private final AnnotationsScannerService annotationsService;

    public Room<? extends Message> addRoom(String roomId, Set<User> users,
                                           RoomInitializer roomInitializer,
                                           Object additionalData) throws ClassNotFoundException {
        Map<String, SocketIOClient> clientsMap = new ConcurrentHashMap<>(users.size());
        List<Timer> gameRoomTimers = new ArrayList<>();


        RoomInitializerData data = new RoomInitializerData(
                roomId, users, new Messenger(clientsMap),
                setupOperations(roomId, gameRoomTimers, clientsMap));
        Room<? extends Message> room = roomInitializer.initialize(data, additionalData);


        AnnotationMethodsParams methodsParams = new AnnotationMethodsParams(Collections.emptyList(),
                server.addNamespace(socketSettings.getNamespace() + "/" + roomId),
                room, clientsMap, annotationsService.getRoomParameters(room));
        HashMap<Class<? extends Annotation>, List<Method>> annotated = annotationsService.gogsAnnotatedMethods(room.getClass());
        annotationsService.setOnMessageListeners(methodsParams.withMethods(annotated.get(OnMessage.class)));
        annotationsService.setOnTickListeners(methodsParams.withMethods(annotated.get(OnTick.class)), gameRoomTimers);
        annotationsService.setUserConnectListener(methodsParams.withMethods(annotated.get(OnConnect.class)));
        annotationsService.setUserDisconnectListener(methodsParams.withMethods(annotated.get(OnDisconnect.class)));


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
            public Timer closeRoomAfterTime(int seconds) {
                return new CloseRoomTimer(this).startCounting(seconds);
            }

            @Override
            public Set<User> connectedUsers() {
                return clientsMap.values().stream().map(u -> (User) u.get("user")).collect(Collectors.toSet());
            }
        };
    }
}

package generic.online.game.server.gogs.api.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.auth.jwt.JwtAuthenticationFilter;
import generic.online.game.server.gogs.model.rooms.*;
import generic.online.game.server.gogs.model.socket.MessageSender;
import generic.online.game.server.gogs.utils.GameRoomInitializer;
import generic.online.game.server.gogs.utils.RoomIdGenerator;
import generic.online.game.server.gogs.utils.settings.CoordinatorSettings;
import generic.online.game.server.gogs.utils.settings.SocketSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class GameFoundService {
    private final CoordinatorSettings coordinatorSettings;
    private final SocketSettings socketSettings;
    private final RoomIdGenerator roomIdGenerator;
    private final Map<String, WaitingRoom> waitingRoomMap;
    private final JwtAuthenticationFilter authenticationFilter;
    private final GameRoomInitializer gameRoomInitializer;

    public boolean acceptBeforeStart() {
        return coordinatorSettings.isAcceptBeforeStart();
    }

    public WaitingRoom createWaitingRoom(Set<User> users, Object additionalData) {
        String roomId = roomIdGenerator.generate();
        int acceptTime = coordinatorSettings.getMaximumAcceptTime();
        Timer timer = new CloseWaitingRoomTimer(roomId, waitingRoomMap).startCounting(acceptTime);
        return new WaitingRoom(roomId, users, timer, additionalData);
    }

    public GameRoom createGameRoom(WaitingRoom waitingRoom) {
        waitingRoom.stopTimer();
        return createGameRoom(waitingRoom.getUsers(), waitingRoom.getAdditionalData());
    }

    public GameRoom createGameRoom(Set<User> users, Object additionalData) {
        String roomId = roomIdGenerator.generate();
        Map<String, SocketIOClient> clientsMap = new ConcurrentHashMap<>(users.size());
        GameRoomInitializerData data = new GameRoomInitializerData(roomId, users, new MessageSender(clientsMap));
        GameRoom gameRoom = gameRoomInitializer.initialize(data, additionalData);
        String namespaceName = socketSettings.getNamespace() + "/" + roomId;
        SocketIONamespace namespace = socketSettings.getServer().addNamespace(namespaceName);
        new GameRoomAnnotationsScanner(namespace, gameRoom).setListeners(clientsMap, authenticationFilter);
        return gameRoom;
    }
}

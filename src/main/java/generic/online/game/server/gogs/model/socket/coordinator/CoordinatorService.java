package generic.online.game.server.gogs.model.socket.coordinator;

import com.corundumstudio.socketio.SocketIOClient;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.queue.Queue;
import generic.online.game.server.gogs.model.rooms.GameRoom;
import generic.online.game.server.gogs.model.rooms.WaitingRoom;
import generic.online.game.server.gogs.model.socket.message.coordinator.CoordinatorMessage;
import generic.online.game.server.gogs.model.socket.message.coordinator.CoordinatorMessageType;
import generic.online.game.server.gogs.utils.SearchBehaviour;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CoordinatorService {
    private final Map<String, SocketIOClient> clientsMap;
    private final Map<String, WaitingRoom> waitingRoomMap;
    private final Map<String, GameRoom> gameRoomMap;
    private final SearchBehaviour searchBehaviour;
    private final GameFoundManager gameFoundManager;

    public void processMessage(User user, CoordinatorMessage message) {
        switch (message.getType()) {
            case SEARCH -> onSearch(user);
            case CANCEL -> onCancel(user);
            case ACCEPT -> onAccept(user, message);
            case DECLINE -> onDecline(user, message);
        }
    }

    private void onSearch(User user) {
        Queue main = searchBehaviour.getQueue();
        Queue after = searchBehaviour.onUserQueue(user);
        switch (after.getStatus()) {
            case WAITING -> {
                CoordinatorMessage message = new CoordinatorMessage();
                message.setType(CoordinatorMessageType.SEARCHING);
                message.setDestination(user.getToken());
                clientsMap.get(user.getToken()).sendEvent(user.getToken(), message.json());
            }
            case FOUND -> {
                Set<User> users = after.getUsers();
                main.remove(users);
                CoordinatorMessage msg = gameFoundManager.createRoom(users);
                after.getUsers().forEach(u -> {
                    msg.setDestination(u.getToken());
                    clientsMap.get(msg.getDestination()).sendEvent(u.getToken(), msg.json());
                });
            }
        }
    }

    public void onCancel(User user) {
        CoordinatorMessage msg = new CoordinatorMessage();
        msg.setDestination(user.getToken());
        msg.setType(CoordinatorMessageType.CANCELED);
        clientsMap.get(user.getToken()).sendEvent(user.getToken(), msg.json());

        Queue main = searchBehaviour.getQueue();
        main.remove(user);
    }

    private void onAccept(User user, CoordinatorMessage message) {
        String uuid = message.getFoundRoomUUID();
        if (uuid == null) {
            return;
        }
        WaitingRoom room = waitingRoomMap.get(uuid);
        room.accept(user);
        if (room.allAccepted()) {
            room.stopTimer();
            gameRoomMap.put(uuid, new GameRoom(room.getUsers()));
            waitingRoomMap.remove(uuid);
            room.getUsers().forEach(u -> {
                CoordinatorMessage msg = new CoordinatorMessage();
                msg.setType(CoordinatorMessageType.FOUND);
                msg.setDestination(u.getToken());
                clientsMap.get(u.getToken()).sendEvent(u.getToken(), msg.json());
            });
        } else {
            CoordinatorMessage msg = new CoordinatorMessage();
            msg.setType(CoordinatorMessageType.ACCEPTED);
            msg.setDestination(user.getToken());
            clientsMap.get(user.getToken()).sendEvent(user.getToken(), msg.json());
        }
    }

    public void onDecline(User user, CoordinatorMessage message) {
        String uuid = message.getFoundRoomUUID();
        if (uuid == null) {
            return;
        }
        WaitingRoom room = waitingRoomMap.get(uuid);
        waitingRoomMap.remove(uuid);
        room.getUsers().forEach(u -> {
            CoordinatorMessage msg = new CoordinatorMessage();
            msg.setType(CoordinatorMessageType.DECLINED);
            msg.setDestination(u.getToken());
            clientsMap.get(u.getToken()).sendEvent(u.getToken(), msg.json());
        });
    }
}

package generic.online.game.server.gogs.api.service;

import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.queue.Queue;
import generic.online.game.server.gogs.model.rooms.GameRoom;
import generic.online.game.server.gogs.model.rooms.WaitingRoom;
import generic.online.game.server.gogs.model.socket.MessageSender;
import generic.online.game.server.gogs.model.socket.coordinator.CoordinatorMessage;
import generic.online.game.server.gogs.utils.SearchBehaviour;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static generic.online.game.server.gogs.model.socket.coordinator.CoordinatorMessageType.*;

@Service
@RequiredArgsConstructor
public class CoordinatorService {
    private final Map<String, WaitingRoom> waitingRoomMap;
    private final SearchBehaviour searchBehaviour;
    private final GameFoundService gameFoundService;
    private final MessageSender messageSender;

    public void onSearch(User user) {
        Queue main = searchBehaviour.getQueue();
        Queue after = searchBehaviour.onUserQueue(user);
        switch (after.getStatus()) {
            case WAITING -> messageSender.sendBack(user.getToken(), new CoordinatorMessage(SEARCHING));
            case FOUND -> {
                Set<User> users = after.getUsers();
                main.remove(users);
                if (gameFoundService.acceptBeforeStart()) {
                    WaitingRoom room = gameFoundService.createWaitingRoom(users, after.getAdditionalData());
                    String roomId = room.getRoomId();
                    waitingRoomMap.put(roomId, room);
                    messageSender.sendBack(room.getUsersTokens(), new CoordinatorMessage(REQUIRE_ACCEPT, roomId));
                } else {
                    GameRoom room = gameFoundService.createGameRoom(users, after.getAdditionalData());
                    List<String> tokens = users.stream().map(User::getToken).collect(Collectors.toUnmodifiableList());
                    messageSender.sendBack(tokens, new CoordinatorMessage(FOUND, room.getRoomId()));
                }
            }
        }
    }

    public void onCancel(User user) {
        messageSender.sendBack(user.getToken(), new CoordinatorMessage(CANCELED));
        searchBehaviour.getQueue().remove(user);
    }

    public void onAccept(User user, String uuid) {
        WaitingRoom srcRoom = waitingRoomMap.get(uuid);
        srcRoom.accept(user);
        if (srcRoom.allAccepted()) {
            GameRoom gr = gameFoundService.createGameRoom(srcRoom);
            waitingRoomMap.remove(uuid);
            messageSender.sendBack(srcRoom.getUsersTokens(), new CoordinatorMessage(FOUND, gr.getRoomId()));
        } else {
            messageSender.sendBack(user.getToken(), new CoordinatorMessage(ACCEPTED));
        }
    }

    public void onDecline(String uuid) {
        WaitingRoom room = waitingRoomMap.get(uuid);
        waitingRoomMap.remove(uuid);
        messageSender.sendBack(room.getUsersTokens(), new CoordinatorMessage(DECLINED));
    }
}

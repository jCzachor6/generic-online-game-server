package generic.online.game.server.gogs.model.socket.coordinator;

import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.rooms.GameRoom;
import generic.online.game.server.gogs.model.rooms.WaitingRoom;
import generic.online.game.server.gogs.model.socket.message.coordinator.CoordinatorMessage;
import generic.online.game.server.gogs.model.socket.message.coordinator.CoordinatorMessageType;
import generic.online.game.server.gogs.settings.CoordinatorSettings;
import generic.online.game.server.gogs.utils.RoomIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

@Component
@RequiredArgsConstructor
public class GameFoundManager {
    private final CoordinatorSettings coordinatorSettings;
    private final RoomIdGenerator roomIdGenerator;
    private final Map<String, WaitingRoom> waitingRoomMap;
    private final Map<String, GameRoom> gameRoomMap;

    public CoordinatorMessage createRoom(Set<User> users) {
        CoordinatorMessage message = new CoordinatorMessage();
        String roomId = roomIdGenerator.generate();
        message.setFoundRoomUUID(roomId);
        if (coordinatorSettings.isAcceptBeforeStart()) {
            message.setType(CoordinatorMessageType.REQUIRE_ACCEPT);
            Timer timer = this.startCounting(roomId);
            waitingRoomMap.put(roomId, new WaitingRoom(users, timer));
        } else {
            message.setType(CoordinatorMessageType.FOUND);
            gameRoomMap.put(roomId, new GameRoom(users));
        }
        return message;
    }

    public Timer startCounting(String roomId) {
        WaitingRoom room = waitingRoomMap.get(roomId);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!room.allAccepted()) {
                    waitingRoomMap.remove(roomId);
                }
            }
        }, coordinatorSettings.getMaximumAcceptTime() * 1000);
        return timer;
    }
}

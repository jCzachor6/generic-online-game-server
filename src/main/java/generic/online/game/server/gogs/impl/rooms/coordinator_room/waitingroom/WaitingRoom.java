package generic.online.game.server.gogs.impl.rooms.coordinator_room.waitingroom;

import generic.online.game.server.gogs.impl.rooms.coordinator_room.CoordinatorMessage;
import generic.online.game.server.gogs.impl.rooms.coordinator_room.OnGameFound;
import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.annotations.OnMessage;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import static generic.online.game.server.gogs.impl.rooms.coordinator_room.CoordinatorMessageType.*;

public class WaitingRoom extends Room {
    private final OnGameFound onGameFound;
    private final int maximumAcceptTime;
    private final Map<String, AcceptanceStatus> acceptanceStatus;

    public WaitingRoom(RoomInitializerData initializerData, OnGameFound onGameFound, int maximumAcceptTime) {
        super(initializerData);
        this.onGameFound = onGameFound;
        this.maximumAcceptTime = maximumAcceptTime;
        this.acceptanceStatus = new ConcurrentHashMap<>();
    }

    public void newAwait(String id, Set<User> users, Object additionalData) {
        Timer timer = new RemoveAcceptanceStatusTimer(id, acceptanceStatus).startCounting(maximumAcceptTime);
        acceptanceStatus.put(id, new AcceptanceStatus(id, additionalData, users, timer));
        getMessenger().sendBack(users, new CoordinatorMessage(REQUIRE_ACCEPT, id));
    }

    @OnMessage("ACCEPT")
    public void onAccept(User user, CoordinatorMessage msg) {
        Optional.ofNullable(this.acceptanceStatus.get(msg.getRoomUUID())).ifPresent(as -> {
            as.accept(user);
            if (as.allAccepted()) {
                onGameFound.create(as.getStatus().keySet(), as.getAdditionalData());
            } else {
                getMessenger().send(user, user, new CoordinatorMessage(ACCEPTED));
            }
        });
    }

    @OnMessage("DECLINE")
    public void onDecline(User user, CoordinatorMessage msg) {
        Optional.ofNullable(this.acceptanceStatus.get(msg.getRoomUUID())).ifPresent(as -> {
            as.getCloseTimer().cancel();
            this.acceptanceStatus.remove(msg.getRoomUUID());
            getMessenger().sendBack(as.getStatus().keySet(), new CoordinatorMessage(DECLINED));
        });
    }
}

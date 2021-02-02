package generic.online.game.server.gogs.impl.rooms.coordinator_room.waitingroom;

import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.impl.rooms.coordinator_room.CoordinatorMessage;
import generic.online.game.server.gogs.impl.rooms.coordinator_room.OnGameFound;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomContext;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import io.javalin.plugin.json.JavalinJson;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import static generic.online.game.server.gogs.impl.rooms.coordinator_room.CoordinatorMessageType.*;

public class WaitingRoom extends Room {
    private static final Class<CoordinatorMessage> COORDINATOR_MESSAGE_CLASS = CoordinatorMessage.class;

    private final OnGameFound onGameFound;
    private final int maximumAcceptTime;
    private final Map<String, AcceptanceStatus> acceptanceStatus;

    public WaitingRoom(RoomInitializerData initializerData, OnGameFound onGameFound, int maximumAcceptTime) {
        super(initializerData);
        this.onGameFound = onGameFound;
        this.maximumAcceptTime = maximumAcceptTime;
        this.acceptanceStatus = new ConcurrentHashMap<>();
    }

    @Override
    public void handlers(RoomContext ctx) {
        ctx.onMessage("ACCEPT", this::handleAcceptMessage);
        ctx.onMessage("DECLINE", this::handleDeclineMessage);
    }

    private void handleAcceptMessage(User user, String body) {
        CoordinatorMessage msg = JavalinJson.fromJson(body, COORDINATOR_MESSAGE_CLASS);
        Optional.ofNullable(this.acceptanceStatus.get(msg.getRoomUUID())).ifPresent(as -> {
            as.accept(user);
            if (as.allAccepted()) {
                onGameFound.create(as.getStatus().keySet(), as.getAdditionalData());
            } else {
                getMessenger().send(user, user, new CoordinatorMessage(ACCEPTED));
            }
        });
    }

    private void handleDeclineMessage(User user, String body) {
        CoordinatorMessage msg = JavalinJson.fromJson(body, COORDINATOR_MESSAGE_CLASS);
        Optional.ofNullable(this.acceptanceStatus.get(msg.getRoomUUID())).ifPresent(as -> {
            as.getCloseTimer().cancel();
            this.acceptanceStatus.remove(msg.getRoomUUID());
            getMessenger().sendBack(as.getStatus().keySet(), new CoordinatorMessage(DECLINED));
        });
    }

    public void newAwait(String id, Set<User> users, Object additionalData) {
        Timer timer = new RemoveAcceptanceStatusTimer(id, acceptanceStatus).startCounting(maximumAcceptTime);
        acceptanceStatus.put(id, new AcceptanceStatus(id, additionalData, users, timer));
        getMessenger().sendBack(users, new CoordinatorMessage(REQUIRE_ACCEPT, id));
    }
}

package generic.online.game.server.gogs.impl.rooms.coordinator_room;

import generic.online.game.server.gogs.model.rooms.UuidGenerator;
import generic.online.game.server.gogs.impl.rooms.coordinator_room.queue.Queue;
import generic.online.game.server.gogs.impl.rooms.coordinator_room.waitingroom.WaitingRoom;
import generic.online.game.server.gogs.impl.rooms.coordinator_room.waitingroom.WaitingRoomData;
import generic.online.game.server.gogs.impl.rooms.coordinator_room.waitingroom.WaitingRoomInitializer;
import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.annotations.InternalRoom;
import generic.online.game.server.gogs.utils.annotations.OnMessage;

import java.util.Set;

import static generic.online.game.server.gogs.impl.rooms.coordinator_room.CoordinatorMessageType.CANCELED;
import static generic.online.game.server.gogs.impl.rooms.coordinator_room.CoordinatorMessageType.SEARCHING;

public class CoordinatorRoom extends Room {
    private final OnGameFound onGameFound;
    private final boolean acceptBeforeStart;
    private final SearchBehaviour searchBehaviour;

    @InternalRoom(prefix = "ROOM")
    private final WaitingRoom waitingRoom;

    public CoordinatorRoom(RoomInitializerData initializerData, OnGameFound onGameFound,
                           SearchBehaviour behaviour, boolean acceptBeforeStart, int maximumAcceptTime) {
        super(initializerData);
        this.onGameFound = onGameFound;
        this.searchBehaviour = behaviour;
        this.acceptBeforeStart = acceptBeforeStart;
        WaitingRoomData data = new WaitingRoomData(maximumAcceptTime, onGameFound);
        this.waitingRoom = (WaitingRoom) new WaitingRoomInitializer().initialize(initializerData, data);
    }

    @OnMessage("SEARCH")
    public void onSearch(User user, CoordinatorMessage msg) {
        Queue main = searchBehaviour.getQueue();
        Queue after = searchBehaviour.onUserQueue(user);
        if (!after.found()) {
            getMessenger().send(user, user, new CoordinatorMessage(SEARCHING));
        } else {
            Set<User> users = after.getUsers();
            main.remove(users);
            decideAndCreateRoom(users, after.getAdditionalData());
        }
    }

    @OnMessage("CANCEL")
    public void onCancel(User user, CoordinatorMessage msg) {
        getMessenger().send(user, user, new CoordinatorMessage(CANCELED));
        searchBehaviour.onUserCancel(user);
    }

    private void decideAndCreateRoom(Set<User> users, Object additionalData) {
        if (acceptBeforeStart) {
            String id = new UuidGenerator(5).generate();
            waitingRoom.newAwait(id, users, additionalData);
        } else {
            onGameFound.create(users, additionalData);
        }
    }
}

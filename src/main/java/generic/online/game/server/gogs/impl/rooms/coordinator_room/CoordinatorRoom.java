package generic.online.game.server.gogs.impl.rooms.coordinator_room;

import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.impl.rooms.coordinator_room.queue.Queue;
import generic.online.game.server.gogs.impl.rooms.coordinator_room.waitingroom.WaitingRoom;
import generic.online.game.server.gogs.impl.rooms.coordinator_room.waitingroom.WaitingRoomData;
import generic.online.game.server.gogs.impl.rooms.coordinator_room.waitingroom.WaitingRoomInitializer;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomContext;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.model.rooms.UuidGenerator;

import java.util.Map;
import java.util.Set;

import static generic.online.game.server.gogs.impl.rooms.coordinator_room.CoordinatorMessageType.CANCELED;
import static generic.online.game.server.gogs.impl.rooms.coordinator_room.CoordinatorMessageType.SEARCHING;

public class CoordinatorRoom extends Room {
    private final OnGameFound onGameFound;
    private final boolean acceptBeforeStart;
    private final SearchBehaviour searchBehaviour;

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

    @Override
    public void internalRooms(Map<String, Room> map) {
        map.put("ROOM", this.waitingRoom);
    }

    @Override
    public void handlers(RoomContext ctx) {
        ctx.onMessage("SEARCH", this::handleSearchMessage);
        ctx.onMessage("CANCEL", this::handleCancelMessage);
    }

    private void handleSearchMessage(User user, String body) {
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

    private void handleCancelMessage(User user, String s) {
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

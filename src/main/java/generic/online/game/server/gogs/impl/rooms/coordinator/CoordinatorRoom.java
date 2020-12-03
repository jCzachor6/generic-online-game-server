package generic.online.game.server.gogs.impl.rooms.coordinator;

import generic.online.game.server.gogs.api.service.RoomManagementService;
import generic.online.game.server.gogs.impl.rooms.coordinator.waiting.WaitingRoom;
import generic.online.game.server.gogs.impl.rooms.coordinator.waiting.WaitingRoomData;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.queue.Queue;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.RoomIdGenerator;
import generic.online.game.server.gogs.utils.RoomInitializer;
import generic.online.game.server.gogs.utils.SearchBehaviour;
import generic.online.game.server.gogs.utils.annotations.OnMessage;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static generic.online.game.server.gogs.impl.rooms.coordinator.CoordinatorMessageType.*;
import static generic.online.game.server.gogs.model.queue.QueueStatus.WAITING;

public class CoordinatorRoom extends Room<CoordinatorMessage> {
    private final RoomManagementService managementService;
    private final boolean acceptBeforeStart;
    private final int maximumAcceptTime;
    private final SearchBehaviour searchBehaviour;
    private final RoomIdGenerator roomIdGenerator;
    private final RoomInitializer roomInitializer;

    private final Map<String, WaitingRoom> waitingRooms = new ConcurrentHashMap<>();

    public CoordinatorRoom(RoomInitializerData initializerData,
                           CoordinatorData data) {
        super(initializerData);
        this.managementService = data.getRoomManagementService();
        this.acceptBeforeStart = data.isAcceptBeforeStart();
        this.maximumAcceptTime = data.getMaximumAcceptTime();
        this.searchBehaviour = data.getSearchBehaviour();
        this.roomIdGenerator = data.getRoomIdGenerator();
        this.roomInitializer = data.getRoomInitializer();
    }

    @OnMessage("SEARCH")
    public void onSearch(User user, CoordinatorMessage msg) throws ClassNotFoundException {
        Queue main = searchBehaviour.getQueue();
        Queue after = searchBehaviour.onUserQueue(user);
        if (WAITING.equals(after.getStatus())) {
            getMessenger().send(user, user, new CoordinatorMessage(SEARCHING));
        } else {
            Set<User> users = after.getUsers();
            main.remove(users);
            decideAndCreateRoom(users, after.getAdditionalData());
        }
    }

    private void decideAndCreateRoom(Set<User> users, Object additionalData) throws ClassNotFoundException {
        String roomId = roomIdGenerator.generate();
        if (acceptBeforeStart) {
            RoomInitializerData data = new RoomInitializerData(roomId, users, getMessenger(), null);
            WaitingRoomData waitingRoomData = new WaitingRoomData(waitingRooms, maximumAcceptTime, roomInitializer, managementService, additionalData);
            WaitingRoom waitingRoom = new WaitingRoom(data, waitingRoomData);
            waitingRooms.put(roomId, waitingRoom);
        } else {
            this.managementService.addRoom(roomId, users, roomInitializer, additionalData);
            getMessenger().sendBack(users, new CoordinatorMessage(FOUND, roomId));
        }
    }

    @OnMessage("CANCEL")
    public void onCancel(User user, CoordinatorMessage msg) {
        getMessenger().send(user, user, new CoordinatorMessage(CANCELED));
        searchBehaviour.onUserCancel(user);
        searchBehaviour.getQueue();
    }

    @OnMessage("ACCEPT")
    public void onAccept(User user, CoordinatorMessage msg) throws ClassNotFoundException {
        String uuid = msg.getRoomUUID();
        WaitingRoom room = waitingRooms.get(uuid);
        room.onAccept(user, msg);
    }

    @OnMessage("DECLINE")
    public void onDecline(User user, CoordinatorMessage msg) {
        String uuid = msg.getRoomUUID();
        WaitingRoom room = waitingRooms.get(uuid);
        room.onDecline(user, msg);
    }
}

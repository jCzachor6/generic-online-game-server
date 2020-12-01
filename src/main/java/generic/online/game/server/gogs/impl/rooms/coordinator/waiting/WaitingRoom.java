package generic.online.game.server.gogs.impl.rooms.coordinator.waiting;

import generic.online.game.server.gogs.api.service.RoomManagementService;
import generic.online.game.server.gogs.impl.rooms.coordinator.CoordinatorMessage;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.RoomInitializer;
import generic.online.game.server.gogs.utils.annotations.OnMessage;

import java.util.Map;
import java.util.Timer;
import java.util.stream.Collectors;

import static generic.online.game.server.gogs.impl.rooms.coordinator.CoordinatorMessageType.*;

public class WaitingRoom extends Room<CoordinatorMessage> {
    private final RoomInitializer roomInitializer;
    private final RoomManagementService managementService;
    private final Object additionalData;

    private final Map<String, WaitingRoom> waitingRooms;
    private final Timer closeTimer;
    private final Map<User, Boolean> acceptanceStatus;

    public WaitingRoom(RoomInitializerData initializerData, WaitingRoomData data) {
        super(initializerData);
        this.managementService = data.getManagementService();
        this.roomInitializer = data.getRoomInitializer();
        this.waitingRooms = data.getWaitingRooms();
        this.additionalData = data.getAdditionalData();
        int acceptTime = data.getMaximumAcceptTime();
        this.closeTimer = new CloseWaitingRoomTimer(getRoomId(), waitingRooms).startCounting(acceptTime);
        this.acceptanceStatus = getGameUsers().stream().collect(Collectors.toMap(x -> x, x -> false));
        getMessenger().sendBack(getGameUsers(), new CoordinatorMessage(REQUIRE_ACCEPT, getRoomId()));
    }

    @OnMessage("ACCEPT")
    public void onAccept(User user, CoordinatorMessage msg) {
        this.acceptanceStatus.replace(user, true);
        if (allAccepted()) {
            this.managementService.addRoom(getRoomId(), getGameUsers(), roomInitializer, additionalData);
            waitingRooms.remove(getRoomId());
            closeTimer.cancel();
            getMessenger().sendBack(getGameUsers(), new CoordinatorMessage(FOUND, getRoomId()));
        } else {
            getMessenger().send(user, user, new CoordinatorMessage(ACCEPTED));
        }
    }

    @OnMessage("DECLINE")
    public void onDecline(User user, CoordinatorMessage msg) {
        closeTimer.cancel();
        waitingRooms.remove(getRoomId());
        getMessenger().sendBack(getGameUsers(), new CoordinatorMessage(DECLINED));
    }

    public boolean allAccepted() {
        return acceptanceStatus.entrySet().stream().allMatch(entry -> entry.getValue().equals(true));
    }
}

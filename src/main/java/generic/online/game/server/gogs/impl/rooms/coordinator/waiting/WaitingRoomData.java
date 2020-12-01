package generic.online.game.server.gogs.impl.rooms.coordinator.waiting;

import generic.online.game.server.gogs.api.service.RoomManagementService;
import generic.online.game.server.gogs.utils.RoomInitializer;
import lombok.Value;

import java.util.Map;

@Value
public class WaitingRoomData {
    Map<String, WaitingRoom> waitingRooms;
    int maximumAcceptTime;

    RoomInitializer roomInitializer;
    RoomManagementService managementService;
    Object additionalData;
}

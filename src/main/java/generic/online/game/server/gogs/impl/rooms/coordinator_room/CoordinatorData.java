package generic.online.game.server.gogs.impl.rooms.coordinator_room;

import generic.online.game.server.gogs.api.service.RoomManagementService;
import generic.online.game.server.gogs.model.rooms.UuidGenerator;
import generic.online.game.server.gogs.utils.RoomInitializer;
import lombok.Value;

@Value
public class CoordinatorData {
    RoomManagementService roomManagementService;
    RoomInitializer<?> roomInitializer;

    boolean acceptBeforeStart;
    int maximumAcceptTime;

    SearchBehaviour searchBehaviour;
    UuidGenerator roomIdGenerator;
}

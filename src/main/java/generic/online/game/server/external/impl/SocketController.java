package generic.online.game.server.external.impl;

import generic.online.game.server.external.impl.game.TttRoomInitializer;
import generic.online.game.server.gogs.api.service.RoomManagementService;
import generic.online.game.server.gogs.impl.RoomUuidGenerator;
import generic.online.game.server.gogs.impl.SimpleSearch;
import generic.online.game.server.gogs.impl.rooms.coordinator.CoordinatorData;
import generic.online.game.server.gogs.impl.rooms.coordinator.CoordinatorRoomInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashSet;

@Controller
public class SocketController {
    private final RoomManagementService managementService;

    @Autowired
    public SocketController(RoomManagementService managementService) throws ClassNotFoundException {
        this.managementService = managementService;
        this.managementService.addRoom("coordinator",
                new HashSet<>(),
                new CoordinatorRoomInitializer(),
                new CoordinatorData(
                        managementService,
                        new TttRoomInitializer(),
                        true,
                        15,
                        new SimpleSearch(2),
                        new RoomUuidGenerator()
                )
        );
    }
}

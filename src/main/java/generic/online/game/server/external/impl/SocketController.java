package generic.online.game.server.external.impl;

import generic.online.game.server.external.impl.game.TttRoomInitializer;
import generic.online.game.server.gogs.api.service.RoomManagementService;
import generic.online.game.server.gogs.impl.RoomUuidGenerator;
import generic.online.game.server.gogs.impl.SimpleSearch;
import generic.online.game.server.gogs.impl.rooms.coordinator_room.CoordinatorData;
import generic.online.game.server.gogs.impl.rooms.coordinator_room.CoordinatorRoomInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
public class SocketController {
    private final RoomManagementService managementService;

    @PostConstruct
    public void init() {
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

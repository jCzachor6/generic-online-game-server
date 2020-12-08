package generic.online.game.server.gogs.impl.rooms.coordinatorroom;

import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.RoomInitializer;

import static generic.online.game.server.gogs.impl.rooms.coordinatorroom.CoordinatorMessageType.FOUND;

public class CoordinatorRoomInitializer implements RoomInitializer<CoordinatorData> {

    @Override
    public Room initialize(RoomInitializerData initializerData, CoordinatorData cd) {
        OnGameFound onGameFound = (users, additionalData) -> {
            String roomId = cd.getRoomIdGenerator().generate();
            cd.getRoomManagementService().addRoom(roomId, users, cd.getRoomInitializer(), additionalData);
            initializerData.getMessenger().sendBack(users, new CoordinatorMessage(FOUND, roomId));
        };

        return new CoordinatorRoom(initializerData, onGameFound,
                cd.getSearchBehaviour(), cd.isAcceptBeforeStart(), cd.getMaximumAcceptTime()
        );
    }
}

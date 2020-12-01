package generic.online.game.server.gogs.impl.rooms.coordinator;

import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.model.socket.Message;
import generic.online.game.server.gogs.utils.RoomInitializer;

public class CoordinatorRoomInitializer implements RoomInitializer<CoordinatorData> {
    @Override
    public Room<? extends Message> initialize(RoomInitializerData initializerData,
                                              CoordinatorData coordinatorData) {
        return new CoordinatorRoom(initializerData, coordinatorData);
    }
}

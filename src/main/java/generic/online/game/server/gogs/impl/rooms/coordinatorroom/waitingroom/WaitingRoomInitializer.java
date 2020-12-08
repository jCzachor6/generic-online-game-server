package generic.online.game.server.gogs.impl.rooms.coordinatorroom.waitingroom;

import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.RoomInitializer;

public class WaitingRoomInitializer implements RoomInitializer<WaitingRoomData> {

    @Override
    public Room initialize(RoomInitializerData initializerData, WaitingRoomData ad) {
        WaitingRoom waitingRoom = new WaitingRoom(initializerData, ad.getOnGameFound(), ad.getMaximumAcceptTime());

        return waitingRoom;
    }
}

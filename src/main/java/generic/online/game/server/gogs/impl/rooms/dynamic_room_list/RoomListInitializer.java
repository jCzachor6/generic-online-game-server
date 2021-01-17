package generic.online.game.server.gogs.impl.rooms.dynamic_room_list;

import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.RoomInitializer;

public class RoomListInitializer implements RoomInitializer<RoomListData> {

    @Override
    public Room initialize(RoomInitializerData initializerData, RoomListData data) {
        return new RoomList(
                initializerData,
                data.getManagementService(),
                data.getRoomMapper(),
                data.getRoomFilter()
        );
    }
}

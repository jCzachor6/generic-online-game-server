package generic.online.game.server.gogs.impl.rooms.dynamic_room_list;

import generic.online.game.server.gogs.api.service.RoomManagementService;
import generic.online.game.server.gogs.model.rooms.Room;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Getter
public class RoomListData<M> {
    private final RoomManagementService managementService;
    private final Function<Room, M> roomMapper;
    private final Predicate<Room> roomFilter;
}

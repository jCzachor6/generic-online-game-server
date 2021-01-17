package generic.online.game.server.gogs.impl.rooms.dynamic_room_list;

import generic.online.game.server.gogs.api.service.RoomManagementService;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.annotations.OnConnect;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static generic.online.game.server.gogs.impl.rooms.dynamic_room_list.RoomListState.*;

public class RoomList extends Room implements DynamicRoomListOperations {
    private final RoomManagementService service;
    private final Predicate<? super Room> filter;
    private final Function<? super Room, ?> mapper;

    public RoomList(RoomInitializerData data,
                    RoomManagementService managementService,
                    Function<? super Room, ?> roomMapper,
                    Predicate<? super Room> roomFilter) {
        super(data);
        service = managementService;
        mapper = roomMapper;
        filter = roomFilter;
    }

    @OnConnect
    public void onConnect(User user) {
        List<?> filtered = this.service.getRooms().stream()
                .filter(filter)
                .map(mapper)
                .collect(Collectors.toList());
        RoomListMessage msg = new RoomListMessage();
        msg.setRooms(filtered);
        msg.setState(ALL);
        getMessenger().send(user, this, msg);
    }

    @Override
    public void created(String roomID) {
        findRoom(roomID).ifPresent(
                (found) -> getMessenger().sendToAll(this, mapSingle(found, CREATED))
        );
    }

    @Override
    public void changed(String roomID) {
        findRoom(roomID).ifPresent(
                (found) -> getMessenger().sendToAll(this, mapSingle(found, UPDATED))
        );
    }

    @Override
    public void removed(String roomID) {
        findRoom(roomID).ifPresent(
                (found) -> getMessenger().sendToAll(this, mapSingle(found, REMOVED))
        );
    }

    private Optional<?> findRoom(String roomID) {
        return this.service.getRooms().stream()
                .filter(filter)
                .filter(r -> r.getRoomId().equals(roomID))
                .findAny()
                .map(mapper);
    }

    private RoomListMessage mapSingle(Object found, RoomListState state) {
        RoomListMessage msg = new RoomListMessage();
        msg.setRooms(Collections.singletonList(found));
        msg.setState(state);
        return msg;
    }
}

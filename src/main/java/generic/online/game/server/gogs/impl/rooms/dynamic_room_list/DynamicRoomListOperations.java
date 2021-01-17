package generic.online.game.server.gogs.impl.rooms.dynamic_room_list;

public interface DynamicRoomListOperations {
    void created(String roomID);

    void changed(String roomID);

    void removed(String roomID);
}

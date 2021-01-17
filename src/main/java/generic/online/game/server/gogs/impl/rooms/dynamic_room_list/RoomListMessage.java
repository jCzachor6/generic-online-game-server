package generic.online.game.server.gogs.impl.rooms.dynamic_room_list;

import generic.online.game.server.gogs.model.socket.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoomListMessage extends Message {
    private RoomListState state;
    private List<?> rooms;
}

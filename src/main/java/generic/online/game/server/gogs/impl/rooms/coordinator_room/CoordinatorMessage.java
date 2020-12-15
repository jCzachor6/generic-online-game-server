package generic.online.game.server.gogs.impl.rooms.coordinator_room;

import generic.online.game.server.gogs.model.socket.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CoordinatorMessage extends Message {
    private CoordinatorMessageType type;
    private String roomUUID;
    private HashMap<String, String> searchCriteria;

    public CoordinatorMessage(CoordinatorMessageType type) {
        this.setType(type);
    }

    public CoordinatorMessage(CoordinatorMessageType type, String roomUUID) {
        this.type = type;
        this.roomUUID = roomUUID;
    }
}

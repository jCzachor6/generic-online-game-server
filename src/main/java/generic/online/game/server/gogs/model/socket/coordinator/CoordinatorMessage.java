package generic.online.game.server.gogs.model.socket.coordinator;

import generic.online.game.server.gogs.model.socket.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CoordinatorMessage extends Message<CoordinatorMessageType> {
    private String foundRoomUUID;
    private HashMap<String, String> searchCriteria;

    public CoordinatorMessage(CoordinatorMessageType type) {
        this.setType(type);
    }

    public CoordinatorMessage(CoordinatorMessageType type, String foundRoomUUID) {
        this.setType(type);
        this.setFoundRoomUUID(foundRoomUUID);
    }
}

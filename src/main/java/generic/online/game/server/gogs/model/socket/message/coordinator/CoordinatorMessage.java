package generic.online.game.server.gogs.model.socket.message.coordinator;

import generic.online.game.server.gogs.model.socket.message.Message;
import generic.online.game.server.gogs.model.socket.message.coordinator.CoordinatorMessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;

@EqualsAndHashCode(callSuper = true)
@Data
public class CoordinatorMessage extends Message<CoordinatorMessageType> {
    private String foundRoomUUID;
    private HashMap<String, String> searchCriteria;
}

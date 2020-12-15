package generic.online.game.server.gogs.impl.rooms.coordinator_room.waitingroom;

import generic.online.game.server.gogs.impl.rooms.coordinator_room.OnGameFound;
import lombok.Value;

@Value
public class WaitingRoomData {
    int maximumAcceptTime;
    OnGameFound onGameFound;
}

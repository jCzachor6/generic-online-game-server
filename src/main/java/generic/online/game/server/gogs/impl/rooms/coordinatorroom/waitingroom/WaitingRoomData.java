package generic.online.game.server.gogs.impl.rooms.coordinatorroom.waitingroom;

import generic.online.game.server.gogs.impl.rooms.coordinatorroom.OnGameFound;
import lombok.Value;

@Value
public class WaitingRoomData {
    int maximumAcceptTime;
    OnGameFound onGameFound;
}

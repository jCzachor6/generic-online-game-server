package generic.online.game.server.gogs.settings;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class GameServerSettings {
    private final int tickRate;
    private final boolean closeEmptyRooms;
    private final boolean allowRejoin;
    private final int maximumRejoinTime;
    private final int maximumJoinTime;
    private final boolean privateRooms;
    private final int roomMaxCapacity;
    private final boolean saveReplays;
}

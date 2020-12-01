package fixtures;

import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.annotations.OnTick;
import lombok.Getter;

@Getter
public class TestRoom extends Room<TestMessage> {
    int tickCount;
    int dtCount;

    public TestRoom(RoomInitializerData data) {
        super(data);
    }

    @OnTick(tickRate = 3)
    public void tick(long dt) {
        tickCount++;
        dtCount += dt;
    }
}

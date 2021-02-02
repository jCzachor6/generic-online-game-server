package fixtures;

import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomContext;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.interfaces.OnConnect;
import generic.online.game.server.gogs.utils.interfaces.OnDisconnect;
import lombok.Getter;

@Getter
public class TestRoom extends Room implements OnConnect, OnDisconnect {
    int tickCount;
    int dtCount;

    public TestRoom(RoomInitializerData data) {
        super(data);
    }

    @Override
    public void handlers(RoomContext ctx) {
        ctx.onTick(3L, this::tick);
        ctx.onMessage("MESSAGE", this::onMessage);
        ctx.onMessage("MESSAGE2", this::onMessage);
    }

    public void tick(long dt) {
        tickCount++;
        dtCount += dt;
    }

    @Override
    public void onConnect(User u) {

    }

    @Override
    public void onDisconnect(User u) {

    }

    public void onMessage(User u, String m) {

    }

    public void notAnnotated() {

    }
}

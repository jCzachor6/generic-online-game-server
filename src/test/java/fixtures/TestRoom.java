package fixtures;

import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.annotations.OnConnect;
import generic.online.game.server.gogs.utils.annotations.OnDisconnect;
import generic.online.game.server.gogs.utils.annotations.OnMessage;
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

    @OnConnect
    public void onConnect(User u) {

    }

    @OnDisconnect
    public void onDisconnect(User u) {

    }

    @OnMessage(value = "MESSAGE")
    public void onMessage(User u, TestMessage m) {

    }

    @OnMessage(value = "MESSAGE2")
    public void onMessage2(User u, TestMessage m) {

    }

    public void notAnnotated() {

    }
}

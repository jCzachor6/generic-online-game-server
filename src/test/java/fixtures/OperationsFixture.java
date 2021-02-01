package fixtures;

import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.model.rooms.Operations;

import java.util.Set;
import java.util.Timer;

public class OperationsFixture implements Operations {
    @Override
    public void closeRoom() {

    }

    @Override
    public Timer closeRoomAfterTime(int seconds) {
        return null;
    }

    @Override
    public Set<User> connectedUsers() {
        return UserFixture.twoUsers();
    }
}

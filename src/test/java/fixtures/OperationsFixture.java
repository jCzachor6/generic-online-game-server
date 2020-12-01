package fixtures;

import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.rooms.Operations;

import java.util.Set;

public class OperationsFixture implements Operations {
    @Override
    public void closeRoom() {

    }

    @Override
    public Set<User> connectedUsers() {
        return Set.of(UserFixture.anonUser(), UserFixture.danyUser());
    }
}

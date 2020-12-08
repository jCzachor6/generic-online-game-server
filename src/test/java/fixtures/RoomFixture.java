package fixtures;

import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.rooms.Operations;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.model.socket.Messenger;

import java.util.Set;

public class RoomFixture {

    public static TestRoom testing() {
        String roomId = "testId";
        Set<User> users = Set.of(UserFixture.anonUser(), UserFixture.danyUser());
        Messenger messenger = new Messenger(null);
        RoomInitializerData data = new RoomInitializerData(roomId, users, messenger).setOperations(new OperationsFixture());
        return new TestRoom(data);
    }
}
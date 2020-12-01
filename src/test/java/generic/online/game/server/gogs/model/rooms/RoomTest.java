package generic.online.game.server.gogs.model.rooms;

import fixtures.OperationsFixture;
import fixtures.UserFixture;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.socket.Messenger;
import org.junit.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomTest {

    @Test
    public void shouldReturnSameFields() {
        String roomId = "testId";
        Set<User> users = Set.of(UserFixture.anonUser(), UserFixture.danyUser());
        Messenger messenger = new Messenger(null);
        Operations operations = new OperationsFixture();

        RoomInitializerData data = new RoomInitializerData(roomId, users, messenger, operations);
        Room<?> subject = new Room<>(data);
        assertEquals(subject.getOperations(), operations);
        assertEquals(subject.getGameUsers(), users);
        assertEquals(subject.getMessenger(), messenger);
        assertEquals(subject.getRoomId(), roomId);
    }

}

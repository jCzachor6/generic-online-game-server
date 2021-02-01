package generic.online.game.server.gogs.model.rooms;

import fixtures.OperationsFixture;
import fixtures.UserFixture;
import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.model.socket.Messenger;
import org.junit.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoomTest {

    @Test
    public void shouldReturnSameFields() {
        String roomId = "testId";
        Set<User> users = UserFixture.twoUsers();
        Messenger messenger = new Messenger(null);
        Operations operations = new OperationsFixture();
        RoomInitializerData data = new RoomInitializerData(roomId, users, messenger).setOperations(operations);

        Room subject = new Room(data);

        assertEquals(subject.getOperations(), operations);
        assertEquals(subject.getGameUsers(), users);
        assertEquals(subject.getMessenger(), messenger);
        assertEquals(subject.getRoomId(), roomId);
        long createdOn = new Date().getTime();
        assertTrue(subject.getCreatedOn().getTime() <= createdOn);
        assertTrue(subject.getCreatedOn().getTime() > createdOn - 1000);
    }
}

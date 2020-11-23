package generic.online.game.server.gogs.model.rooms;

import fixtures.UserFixture;
import generic.online.game.server.gogs.model.auth.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WaitingRoomTest {
    WaitingRoom waitingRoom;

    @Before
    public void init() {
        Set<User> users = Set.of(UserFixture.anonUser(), UserFixture.danyUser());
        waitingRoom = new WaitingRoom("id", users, null, null);
    }

    @Test
    public void noneAcceptedAtStart() {
        assertTrue(waitingRoom.noneAccepted());
    }

    @Test
    public void allAcceptedAfterAllUsersAccept() {
        waitingRoom.accept(UserFixture.anonUser());
        waitingRoom.accept(UserFixture.danyUser());
        assertTrue(waitingRoom.allAccepted());
    }

    @Test
    public void getUsersShouldReturnSameUsersAsAtStart() {
        assertEquals(2, waitingRoom.getUsers().size());
        assertTrue(waitingRoom.getUsers().contains(UserFixture.anonUser()));
        assertTrue(waitingRoom.getUsers().contains(UserFixture.danyUser()));
    }
}

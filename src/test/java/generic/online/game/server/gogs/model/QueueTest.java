package generic.online.game.server.gogs.model;

import generic.online.game.server.gogs.model.queue.Queue;
import generic.online.game.server.gogs.model.queue.QueueStatus;
import generic.online.game.server.gogs.model.user.User;
import fixtures.QueueFixture;
import fixtures.UserFixture;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

public class QueueTest {
    @Test
    public void shouldCreateQueue() {
        Queue subject = QueueFixture.twoUserQueue();
        assertEquals(2, subject.size());
        TestCase.assertEquals(QueueStatus.WAITING, subject.getStatus());
    }

    @Test
    public void shouldNotAddSameUser() {
        Queue subject = QueueFixture.twoUserQueue();
        subject.add(UserFixture.anonUser());
        assertEquals(2, subject.size());
    }

    @Test
    public void shouldRemoveUser() {
        Queue subject = QueueFixture.twoUserQueue();
        subject.remove(UserFixture.anonUser());
        assertEquals(1, subject.size());
    }

    @Test
    public void shouldRemoveGroupOfUsers() {
        Queue subject = QueueFixture.twoUserQueue();
        Set<User> userSet = new HashSet<>();
        userSet.add(UserFixture.anonUser());
        userSet.add(UserFixture.danyUser());
        subject.remove(userSet);
        assertEquals(0, subject.size());
    }

    @Test
    public void shouldChangeStatusOnFound(){
        Queue queue = QueueFixture.twoUserQueue();
        Queue subject = queue.found(new HashSet<>());
        assertEquals(0, subject.size());
        assertEquals(QueueStatus.FOUND, subject.getStatus());
    }
}
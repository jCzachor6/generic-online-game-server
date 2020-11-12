package czachor.jakub.ggs.impl;

import czachor.jakub.ggs.model.queue.Queue;
import czachor.jakub.ggs.model.queue.QueueStatus;
import fixtures.UserFixture;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class SimpleSearchTest {

    @Test
    public void shouldNotFind() {
        SimpleSearch subject = new SimpleSearch(2);
        Queue queue = subject.onUserQueue(UserFixture.anonUser());
        assertEquals(QueueStatus.WAITING, queue.getStatus());
    }

    @Test
    public void shouldFindOnTwoUsers() {
        SimpleSearch subject = new SimpleSearch(2);
        subject.onUserQueue(UserFixture.anonUser());
        Queue queue = subject.onUserQueue(UserFixture.danyUser());
        assertEquals(QueueStatus.FOUND, queue.getStatus());
    }

}

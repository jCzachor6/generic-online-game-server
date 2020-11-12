package fixtures;

import czachor.jakub.ggs.model.queue.Queue;
import czachor.jakub.ggs.model.queue.QueueStatus;
import czachor.jakub.ggs.model.user.User;

import java.util.HashSet;
import java.util.Set;

public class QueueFixture {
    public static Queue twoUserQueue() {
        Set<User> userSet = new HashSet<>();
        userSet.add(UserFixture.anonUser());
        userSet.add(UserFixture.danyUser());
        return Queue.builder()
                .status(QueueStatus.WAITING)
                .users(userSet)
                .build();
    }
}

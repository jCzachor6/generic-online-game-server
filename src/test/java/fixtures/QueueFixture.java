package fixtures;

import generic.online.game.server.gogs.model.queue.Queue;
import generic.online.game.server.gogs.model.queue.QueueStatus;
import generic.online.game.server.gogs.model.user.User;

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

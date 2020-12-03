package generic.online.game.server.gogs.impl;

import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.queue.Queue;
import generic.online.game.server.gogs.model.queue.QueueStatus;
import generic.online.game.server.gogs.utils.SearchBehaviour;
import lombok.ToString;

import java.util.HashSet;

@ToString
public class SimpleSearch implements SearchBehaviour {
    private final int usersCapacity;
    private final Queue queue;

    public SimpleSearch(int usersCapacity) {
        this.usersCapacity = usersCapacity;
        this.queue = Queue.builder()
                .users(new HashSet<>())
                .status(QueueStatus.WAITING)
                .build();
    }

    @Override
    public Queue getQueue() {
        return this.queue;
    }

    @Override
    public Queue onUserQueue(User user) {
        queue.add(user);

        if (queue.size() == usersCapacity) {
            return queue.found(queue.getUsers());
        }

        return queue;
    }

    @Override
    public Queue onUserCancel(User user) {
        queue.remove(user);
        return queue;
    }
}

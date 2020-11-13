package generic.online.game.server.gogs.utils;

import generic.online.game.server.gogs.model.queue.Queue;
import generic.online.game.server.gogs.model.queue.QueueStatus;
import generic.online.game.server.gogs.model.user.User;
import lombok.Getter;

import java.util.HashSet;

@Getter
public abstract class SearchCriteria {
    protected Queue queue;

    public SearchCriteria() {
        this.queue = Queue.builder()
                .users(new HashSet<>())
                .status(QueueStatus.WAITING)
                .build();
    }

    public abstract Queue onUserQueue(User user);
}

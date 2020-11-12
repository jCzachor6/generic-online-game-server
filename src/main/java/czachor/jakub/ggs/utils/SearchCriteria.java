package czachor.jakub.ggs.utils;

import czachor.jakub.ggs.model.queue.Queue;
import czachor.jakub.ggs.model.queue.QueueStatus;
import czachor.jakub.ggs.model.user.User;
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

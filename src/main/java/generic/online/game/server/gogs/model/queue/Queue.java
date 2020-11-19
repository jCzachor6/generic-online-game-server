package generic.online.game.server.gogs.model.queue;

import generic.online.game.server.gogs.model.auth.User;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
public class Queue {
    private final Set<User> users;
    private final QueueStatus status;

    public Queue found(Set<User> users) {
        return Queue.builder()
                .users(new HashSet<>(users))
                .status(QueueStatus.FOUND)
                .build();
    }

    public void add(User user) {
        users.add(user);
    }

    public void remove(User user) {
        users.remove(user);
    }

    public void remove(Set<User> users) {
        this.users.removeAll(users);
    }

    public int size() {
        return users.size();
    }
}

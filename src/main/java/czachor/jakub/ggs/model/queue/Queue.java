package czachor.jakub.ggs.model.queue;

import czachor.jakub.ggs.model.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class Queue {
    private final Set<User> users;
    private final QueueStatus status;

    public Queue found(Set<User> users) {
        return Queue.builder()
                .users(users)
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

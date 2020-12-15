package generic.online.game.server.gogs.impl.rooms.coordinator_room.queue;

import generic.online.game.server.gogs.model.auth.User;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

import static generic.online.game.server.gogs.impl.rooms.coordinator_room.queue.QueueStatus.FOUND;

@Builder
@Getter
@EqualsAndHashCode
public class Queue {
    private final Set<User> users;
    private final QueueStatus status;
    private final Object additionalData;

    public Queue found(Set<User> users) {
        return Queue.builder()
                .users(new HashSet<>(users))
                .status(FOUND)
                .build();
    }

    public Queue found(Set<User> users, Object additionalData) {
        return Queue.builder()
                .users(new HashSet<>(users))
                .status(FOUND)
                .additionalData(additionalData)
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

    public boolean found() {
        return FOUND.equals(this.status);
    }
}

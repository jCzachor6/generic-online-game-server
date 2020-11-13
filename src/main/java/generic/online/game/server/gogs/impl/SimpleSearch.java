package generic.online.game.server.gogs.impl;

import generic.online.game.server.gogs.model.queue.Queue;
import generic.online.game.server.gogs.model.user.User;
import generic.online.game.server.gogs.utils.SearchCriteria;
import lombok.ToString;

@ToString
public class SimpleSearch extends SearchCriteria {
    private final int usersCapacity;

    public SimpleSearch(int usersCapacity) {
        this.usersCapacity = usersCapacity;
    }

    @Override
    public Queue onUserQueue(User user) {
        queue.add(user);

        if (queue.size() == usersCapacity) {
            return queue.found(queue.getUsers());
        }

        return queue;
    }
}

package czachor.jakub.ggs.impl;

import czachor.jakub.ggs.model.queue.Queue;
import czachor.jakub.ggs.model.user.User;
import czachor.jakub.ggs.utils.SearchCriteria;
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

package generic.online.game.server.gogs.utils;

import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.queue.Queue;

public interface SearchBehaviour {
    Queue getQueue();
    Queue onUserQueue(User user);
}
package generic.online.game.server.gogs.impl.rooms.coordinator_room;

import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.impl.rooms.coordinator_room.queue.Queue;

public interface SearchBehaviour {
    Queue getQueue();
    Queue onUserQueue(User user);
    Queue onUserCancel(User user);
}

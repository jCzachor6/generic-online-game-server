package generic.online.game.server.gogs.model.rooms;

import generic.online.game.server.gogs.model.auth.User;

import java.util.Set;
import java.util.Timer;

public interface Operations {
    void closeRoom();

    Timer closeRoomAfterTime(int seconds);

    Set<User> connectedUsers();
}

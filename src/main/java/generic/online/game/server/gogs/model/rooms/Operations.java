package generic.online.game.server.gogs.model.rooms;

import generic.online.game.server.gogs.model.auth.User;

import java.util.Set;
import java.util.Timer;

public interface Operations {
    void closeRoom();

    default Timer closeRoomAfterTime(int seconds) {
        return new CloseRoomTimer(this).startCounting(seconds);
    }

    Set<User> connectedUsers();
}

package generic.online.game.server.gogs.model.rooms;

import java.util.Timer;

public interface Operations {
    void closeRoom();

    default Timer closeRoomAfterTime(int seconds) {
        return new CloseGameRoomTimer(this).startCounting(seconds);
    }
}

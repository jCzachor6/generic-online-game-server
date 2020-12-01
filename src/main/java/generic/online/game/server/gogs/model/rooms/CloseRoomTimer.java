package generic.online.game.server.gogs.model.rooms;

import lombok.RequiredArgsConstructor;

import java.util.Timer;
import java.util.TimerTask;

@RequiredArgsConstructor
public class CloseRoomTimer extends TimerTask {
    private final Operations operations;

    @Override
    public void run() {
        operations.closeRoom();
    }

    public Timer startCounting(int seconds) {
        Timer timer = new Timer();
        timer.schedule(this, seconds * 1000);
        return timer;
    }
}

package generic.online.game.server.gogs.impl.rooms.coordinator.waiting;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@RequiredArgsConstructor
public class CloseWaitingRoomTimer extends TimerTask {
    private final String roomId;
    private final Map<String, WaitingRoom> waitingRoomMap;

    @Override
    public void run() {
        WaitingRoom wr = waitingRoomMap.get(roomId);
        if (wr != null && !waitingRoomMap.get(roomId).allAccepted()) {
            waitingRoomMap.remove(roomId);
        }
    }

    public Timer startCounting(int acceptTime) {
        Timer timer = new Timer();
        timer.schedule(this, acceptTime * 1000);
        return timer;
    }
}

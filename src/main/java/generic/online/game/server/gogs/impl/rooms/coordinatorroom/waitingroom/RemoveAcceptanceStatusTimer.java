package generic.online.game.server.gogs.impl.rooms.coordinatorroom.waitingroom;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@RequiredArgsConstructor
public class RemoveAcceptanceStatusTimer extends TimerTask {
    private final String id;
    private final Map<String, AcceptanceStatus> acceptanceStatus;

    @Override
    public void run() {
        AcceptanceStatus wr = acceptanceStatus.get(id);
        if (wr != null && !wr.allAccepted()) {
            acceptanceStatus.remove(id);
        }
    }

    public Timer startCounting(int acceptTime) {
        Timer timer = new Timer();
        timer.schedule(this, acceptTime * 1000);
        return timer;
    }
}

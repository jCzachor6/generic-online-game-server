package generic.online.game.server.gogs.model.rooms;

import generic.online.game.server.gogs.utils.interfaces.TickHandler;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@RequiredArgsConstructor
public class TickRateTimer extends TimerTask {
    private final TickHandler tickHandler;
    private Date currentTime;

    @Override
    public void run() {
        Date newDate = new Date();
        tickHandler.handleTick(newDate.getTime() - currentTime.getTime());
        currentTime = newDate;
    }

    public Timer startTicking(long frequency) {
        Timer timer = new Timer();
        currentTime = new Date();
        timer.scheduleAtFixedRate(this, 0, frequency);
        return timer;
    }
}

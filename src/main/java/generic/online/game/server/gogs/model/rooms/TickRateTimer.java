package generic.online.game.server.gogs.model.rooms;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@RequiredArgsConstructor
public class TickRateTimer extends TimerTask {
    private final Method tickRateMethod;
    private final Room room;
    private Date currentTime;

    @Override
    public void run() {
        try {
            Date newDate = new Date();
            tickRateMethod.invoke(room, newDate.getTime() - currentTime.getTime());
            currentTime = newDate;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Timer startTicking(int frequency) {
        Timer timer = new Timer();
        currentTime = new Date();
        timer.scheduleAtFixedRate(this, 0, frequency);
        return timer;
    }
}

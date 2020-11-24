package generic.online.game.server.gogs.model.rooms;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

@RequiredArgsConstructor
public class TickRateTimer extends TimerTask {
    private final Method tickRateMethod;
    private final GameRoom gameRoom;

    @Override
    public void run() {
        try {
            tickRateMethod.invoke(gameRoom);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Timer startTicking(int frequency) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(this, 0, frequency);
        return timer;
    }
}

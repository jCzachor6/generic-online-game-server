package generic.online.game.server.gogs.model.rooms;

import lombok.SneakyThrows;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Timer;

public class CloseRoomTimerTest {
    public Operations operations = Mockito.mock(Operations.class);

    @Test
    @SneakyThrows
    public void shouldCallOperationsCloseRoom() {
        Timer timer = new CloseRoomTimer(operations).startCounting(1);
        Thread.sleep(1200);
        Mockito.verify(operations).closeRoom();
    }
}

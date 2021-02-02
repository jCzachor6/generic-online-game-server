package generic.online.game.server.gogs.model.rooms;

import fixtures.RoomFixture;
import fixtures.TestRoom;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Timer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TickRateTimerTest {
    @Test
    @SneakyThrows
    public void shouldCallTickMethod() {
        TestRoom testRoom = RoomFixture.testing();
        Method m = MethodUtils.getMatchingMethod(TestRoom.class, "tick", long.class);
        Timer subject = new TickRateTimer(testRoom::tick).startTicking(1000 / 3);
        Thread.sleep(850);
        subject.cancel();
        assertEquals(3, testRoom.getTickCount());
        assertTrue(testRoom.getDtCount() < 700);
    }
}

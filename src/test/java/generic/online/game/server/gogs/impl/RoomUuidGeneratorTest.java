package generic.online.game.server.gogs.impl;

import generic.online.game.server.gogs.utils.RoomIdGenerator;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class RoomUuidGeneratorTest {

    @Test
    public void shouldGenerateUUID() {
        RoomIdGenerator generator = new RoomUuidGenerator();
        String result = generator.generate();
        assertNotNull(result);
        assertEquals(32, result.length());
    }

}

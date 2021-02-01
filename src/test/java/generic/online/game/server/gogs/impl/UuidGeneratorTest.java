package generic.online.game.server.gogs.impl;

import generic.online.game.server.gogs.model.rooms.UuidGenerator;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class UuidGeneratorTest {

    @Test
    public void shouldGenerateUUID() {
        UuidGenerator generator = new UuidGenerator(10);
        String result = generator.generate();
        assertNotNull(result);
        assertEquals(10, result.length());
    }
}

package generic.online.game.server.gogs.impl;


import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class PinPasswordGeneratorTest {

    @Test
    public void shouldGenerateFourDigitPin() {
        PinPasswordGenerator generator = new PinPasswordGenerator();
        String result = generator.generate();
        assertNotNull(result);
        assertEquals(4, result.length());
    }

}

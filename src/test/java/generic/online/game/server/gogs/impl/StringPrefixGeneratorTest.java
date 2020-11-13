package generic.online.game.server.gogs.impl;

import generic.online.game.server.gogs.utils.AnonymousPrefixGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringPrefixGeneratorTest {

    @Test
    public void shouldReturnPrefixFromConstructor() {
        String prefix = "test_";
        AnonymousPrefixGenerator prefixGenerator = new StringPrefixGenerator(prefix);
        assertEquals(prefix, prefixGenerator.generate());
    }
}

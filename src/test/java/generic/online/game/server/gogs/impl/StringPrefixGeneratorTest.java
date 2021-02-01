package generic.online.game.server.gogs.impl;

import generic.online.game.server.gogs.api.auth.model.StringUsernameGenerator;
import generic.online.game.server.gogs.utils.AnonymousUsernameGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringPrefixGeneratorTest {

    @Test
    public void shouldReturnPrefixFromConstructor() {
        String prefix = "test_";
        AnonymousUsernameGenerator prefixGenerator = new StringUsernameGenerator(prefix);
        assertEquals(prefix + 1, prefixGenerator.generate());
        assertEquals(prefix + 2, prefixGenerator.generate());
    }
}

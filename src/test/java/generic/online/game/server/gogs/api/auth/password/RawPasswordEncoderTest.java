package generic.online.game.server.gogs.api.auth.password;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RawPasswordEncoderTest {

    private final PasswordEncoder encoder = new RawPasswordEncoder();

    @Test
    void testEncodeReturnsSameString() {
        String rawPassword = "password123";
        assertEquals(rawPassword, encoder.encode(rawPassword));
    }

    @Test
    void testMatchingPasswords() {
        String rawPassword = "securePass";
        String encodedPassword = encoder.encode(rawPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void testNonMatchingPasswords() {
        String rawPassword = "correctPassword";
        String encodedPassword = encoder.encode("differentPassword");
        assertFalse(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void testEmptyPassword() {
        assertTrue(encoder.matches("", encoder.encode("")));
    }
}

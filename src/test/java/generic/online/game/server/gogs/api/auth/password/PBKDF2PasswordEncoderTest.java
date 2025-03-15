package generic.online.game.server.gogs.api.auth.password;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PBKDF2PasswordEncoderTest {

    private final PasswordEncoder encoder = new PBKDF2PasswordEncoder();

    @Test
    void testEncodeProducesDifferentHashesForSamePassword() {
        String password = "securePassword";
        String hash1 = encoder.encode(password);
        String hash2 = encoder.encode(password);

        assertNotEquals(hash1, hash2, "Hashes should be different due to salting.");
    }

    @Test
    void testMatchingPassword() {
        String password = "mySecretPass";
        String encodedPassword = encoder.encode(password);

        assertTrue(encoder.matches(password, encodedPassword), "Password should match the stored hash.");
    }

    @Test
    void testNonMatchingPassword() {
        String password = "correctPassword";
        String differentPassword = "wrongPassword";
        String encodedPassword = encoder.encode(password);

        assertFalse(encoder.matches(differentPassword, encodedPassword), "Different passwords should not match.");
    }

    @Test
    void testEmptyPassword() {
        String encodedPassword = encoder.encode("");
        assertTrue(encoder.matches("", encodedPassword), "Empty password should match its own hash.");
    }
}

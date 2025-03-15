package generic.online.game.server.gogs.api.auth.password;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PBKDF2PasswordEncoder implements PasswordEncoder {

    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    @Override
    public String encode(String rawPassword) {
        byte[] salt = generateSalt();
        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        String hash = hashPassword(rawPassword, salt);
        return saltBase64 + ":" + hash;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        String[] parts = encodedPassword.split(":");
        if (parts.length != 2) {
            return false;
        }
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        String expectedHash = parts[1];
        return hashPassword(rawPassword, salt).equals(expectedHash);
    }

    private String hashPassword(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }
}

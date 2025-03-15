package generic.online.game.server.gogs.api.auth.password;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SaltedSHA256PasswordEncoder implements PasswordEncoder {
    private static final int SALT_LENGTH = 16;

    @Override
    public String encode(String rawPassword) {
        byte[] salt = generateSalt();
        String saltHex = bytesToHex(salt);
        String hash = hashPassword(rawPassword, salt);
        return saltHex + ":" + hash;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        String[] parts = encodedPassword.split(":");
        if (parts.length != 2) {
            return false;
        }
        byte[] salt = hexToBytes(parts[0]);
        String expectedHash = parts[1];
        return hashPassword(rawPassword, salt).equals(expectedHash);
    }

    private String hashPassword(String password, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            byte[] hash = digest.digest(password.getBytes());
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}


package generic.online.game.server.gogs.api.auth.jwt.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import generic.online.game.server.gogs.api.auth.jwt.model.JwtClaims;
import generic.online.game.server.gogs.api.auth.jwt.model.JwtToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class JwtHmacSha256AlgorithmImplTest {

    private JwtHmacSha256AlgorithmImpl jwtTokenAlgorithm;
    private String secret;
    private Long expirationTime;
    private String id;
    private String username;
    private List<String> roles;

    @BeforeEach
    void setUp() {
        jwtTokenAlgorithm = new JwtHmacSha256AlgorithmImpl();
        secret = "mySecretKey";
        expirationTime = 3600000L;
        id = "123";
        username = "testUser";
        roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
    }

    @Test
    void testGenerateToken() throws JsonProcessingException {
        JwtToken token = jwtTokenAlgorithm.generateToken(secret, expirationTime, id, username, roles);

        assertNotNull(token);
        assertTrue(token.value().split("\\.").length == 3, "Token should consist of 3 parts.");
    }

    @Test
    void testValidateToken_validToken() throws JsonProcessingException {
        JwtToken token = jwtTokenAlgorithm.generateToken(secret, expirationTime, id, username, roles);

        boolean isValid = jwtTokenAlgorithm.validateToken(secret, token);

        assertTrue(isValid, "Token should be valid.");
    }

    @Test
    void testValidateToken_invalidToken() throws JsonProcessingException {
        JwtToken token = jwtTokenAlgorithm.generateToken(secret, expirationTime, id, username, roles);

        String invalidTokenString = token.value().replace("e", "x");
        JwtToken invalidToken = new JwtToken(invalidTokenString);

        boolean isValid = jwtTokenAlgorithm.validateToken(secret, invalidToken);

        assertFalse(isValid, "Token should be invalid.");
    }

    @Test
    void testGetClaims() throws JsonProcessingException {
        JwtToken token = jwtTokenAlgorithm.generateToken(secret, expirationTime, id, username, roles);

        JwtClaims claims = jwtTokenAlgorithm.getClaims(secret, token);

        assertNotNull(claims);
        assertEquals(username, claims.getUsername(), "Username should match.");
        assertEquals(id, claims.getId(), "ID should match.");
        assertTrue(claims.getRoles().contains("ROLE_USER"), "Roles should contain 'ROLE_USER'.");
    }

    @Test
    void testExpiredToken() throws JsonProcessingException {
        Long expiredExpirationTime = -1L;
        JwtToken expiredToken = jwtTokenAlgorithm.generateToken(secret, expiredExpirationTime, id, username, roles);

        boolean isValid = jwtTokenAlgorithm.validateToken(secret, expiredToken);

        assertFalse(isValid, "Token should be invalid due to expiration.");
    }
}

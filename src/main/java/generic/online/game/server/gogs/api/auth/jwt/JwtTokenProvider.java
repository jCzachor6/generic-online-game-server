package generic.online.game.server.gogs.api.auth.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import generic.online.game.server.gogs.api.auth.jwt.impl.JwtTokenAlgorithm;
import generic.online.game.server.gogs.api.auth.jwt.model.JwtClaims;
import generic.online.game.server.gogs.api.auth.jwt.model.JwtToken;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class JwtTokenProvider {
    private final String secret;
    private final Long expirationInMilliseconds;
    private final JwtTokenAlgorithm algorithm;

    public JwtToken generateToken(String id, String username, List<String> roles) throws JsonProcessingException {
        return algorithm.generateToken(secret, expirationInMilliseconds, id, username, roles);
    }

    public JwtClaims getClaims(JwtToken token) {
        return algorithm.getClaims(secret, token);
    }

    public boolean validateToken(JwtToken token) {
        return algorithm.validateToken(secret, token);
    }
}

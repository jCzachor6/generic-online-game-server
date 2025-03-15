package generic.online.game.server.gogs.api.auth.jwt.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import generic.online.game.server.gogs.api.auth.jwt.model.JwtClaims;
import generic.online.game.server.gogs.api.auth.jwt.model.JwtToken;

import java.util.List;

public interface JwtTokenAlgorithm {
    JwtToken generateToken(String secret, Long expiration, String id, String username, List<String> roles) throws JsonProcessingException;
    JwtClaims getClaims(String secret, JwtToken token);
    boolean validateToken(String secret, JwtToken token);
}

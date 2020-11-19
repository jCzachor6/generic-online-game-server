package generic.online.game.server.gogs.model.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Optional;

@RequiredArgsConstructor
public class JwtTokenRetriever {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final String bearerToken;

    public Optional<String> getToken() {
        if (!StringUtils.isEmpty(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return Optional.of(bearerToken.substring(BEARER_PREFIX.length()));
        }
        return Optional.empty();
    }
}

package generic.online.game.server.gogs.model.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtTokenRetriever {
    private static final String BEARER_PREFIX = "Bearer ";
    private final HttpServletRequest request;

    public Optional<String> getToken() {
        String bearerToken = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return Optional.of(bearerToken.substring(BEARER_PREFIX.length()));
        }
        return Optional.empty();
    }
}

package generic.online.game.server.gogs.api.auth.jwt;

import com.corundumstudio.socketio.HandshakeData;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@RequiredArgsConstructor
public class JwtTokenRetriever {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_PARAM = "token";
    private static final String BEARER_PREFIX = "Bearer ";

    public String getAuthorizationHeaderValue(Context context) {
        return context.header(AUTHORIZATION_HEADER);
    }

    public String getAuthorizationParamValue(HandshakeData handshakeData) {
        return handshakeData.getSingleUrlParam(AUTHORIZATION_PARAM);
    }

    public Optional<String> getToken(String bearerToken) {
        if (StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return Optional.of(bearerToken.substring(BEARER_PREFIX.length()));
        }
        return Optional.empty();
    }
}

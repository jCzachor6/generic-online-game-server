package generic.online.game.server.gogs.api.auth.jwt;

import com.corundumstudio.socketio.HandshakeData;
import generic.online.game.server.gogs.api.auth.AnonymousController;
import generic.online.game.server.gogs.api.auth.jwt.model.JwtClaims;
import generic.online.game.server.gogs.api.auth.jwt.model.JwtToken;
import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.utils.GogsUserService;
import io.javalin.Javalin;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter {
    private final JwtTokenRetriever jwtTokenRetriever = new JwtTokenRetriever();

    private final Javalin javalin;
    private final JwtTokenProvider jwtTokenProvider;
    private final GogsUserService userService;

    @PostConstruct
    protected void doFilterInternal() {
        javalin.before(ctx -> {
            String authorizationHeaderValue = jwtTokenRetriever.getAuthorizationHeaderValue(ctx);
            Optional<String> jwtToken = jwtTokenRetriever.getToken(authorizationHeaderValue);
            jwtToken.map(JwtToken::new).ifPresent(token -> {
                if (!token.isBlank() && jwtTokenProvider.validateToken(token)) {
                    JwtClaims claims = jwtTokenProvider.getClaims(token);
                    User user = getUser(token, claims);
                    if (!user.roles().contains(AnonymousController.PROFILE)) {
                        user = userService.map(userService.getOneById(user.id()));
                    }
                    if (user == null) {
                        ctx.status(401).result("Unauthorized");
                        return;
                    }
                    ctx.attribute("user", user);
                }
            });
        });
    }

    public boolean doFilterSocketToken(HandshakeData handshakeData) {
        var token = jwtTokenRetriever.getAuthorizationParamValue(handshakeData);
        if (!token.isBlank() && jwtTokenProvider.validateToken(token)) {
            User user = getUser(token);
            boolean isAnonymous = user.roles().contains(AnonymousController.PROFILE);
            return isAnonymous || userService.getOneById(user.id()) != null;
        }
        return false;
    }

    public User getUser(JwtToken token) {
        JwtClaims claims = jwtTokenProvider.getClaims(token);
        return getUser(token, claims);
    }

    public User getUser(JwtToken token, JwtClaims claims) {
        return new User(
                token.value(),
                claims.getId(),
                claims.getUsername(),
                StringUtils.EMPTY,
                claims.getRoles()
        );
    }
}

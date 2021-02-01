package generic.online.game.server.gogs.api.auth.jwt;

import com.corundumstudio.socketio.HandshakeData;
import generic.online.game.server.gogs.api.auth.AnonymousController;
import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.utils.GogsUserService;
import io.javalin.Javalin;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
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
            jwtToken.ifPresent(token -> {
                if (StringUtils.isNotBlank(token) && jwtTokenProvider.validateToken(token)) {
                    Claims claims = jwtTokenProvider.getClaims(token);
                    User user = getUser(token, claims);
                    if (!user.getRoles().contains(AnonymousController.PROFILE)) {
                        user = userService.map(userService.getOneById(user.getId()));
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
        String token = jwtTokenRetriever.getAuthorizationParamValue(handshakeData);
        if (StringUtils.isNotBlank(token) && jwtTokenProvider.validateToken(token)) {
            User user = getUser(token);
            boolean isAnonymous = user.getRoles().contains(AnonymousController.PROFILE);
            return isAnonymous || userService.getOneById(user.getId()) != null;
        }
        return false;
    }

    public User getUser(String token) {
        Claims claims = jwtTokenProvider.getClaims(token);
        return getUser(token, claims);
    }

    public User getUser(String token, Claims claims) {
        return new User(
                token,
                (String) claims.get(User.Fields.id),
                (String) claims.get(User.Fields.username),
                StringUtils.EMPTY,
                Arrays.asList(StringUtils.split((String) claims.get(User.Fields.roles)))
        );
    }
}

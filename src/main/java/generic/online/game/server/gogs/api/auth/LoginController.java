package generic.online.game.server.gogs.api.auth;

import generic.online.game.server.gogs.api.auth.model.AuthRequest;
import generic.online.game.server.gogs.api.auth.model.AuthResponse;
import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.api.auth.service.AuthenticationService;
import io.javalin.Javalin;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@Profile(LoginController.PROFILE)
@RequiredArgsConstructor
public class LoginController {
    public final static String PROFILE = "AUTH_LOGIN";

    private final Javalin javalin;
    private final AuthenticationService authenticationService;

    @PostConstruct
    private void init() {
        javalin.post("/gogs/api/auth/login", (ctx) -> {
            AuthRequest request = ctx.bodyAsClass(AuthRequest.class);
            AuthResponse response = new AuthResponse(authenticationService.authenticateUser(request));
            ctx.status(HttpStatus.OK_200).json(response);
        });
    }
}

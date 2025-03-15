package generic.online.game.server.gogs.api.auth;

import generic.online.game.server.gogs.api.auth.model.AuthRequest;
import generic.online.game.server.gogs.api.auth.model.AuthResponse;
import generic.online.game.server.gogs.api.auth.service.AuthenticationService;
import io.javalin.Javalin;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@Profile(RegisterController.PROFILE)
@RequiredArgsConstructor
public class RegisterController {
    public final static String PROFILE = "AUTH_LOGIN";

    private final Javalin javalin;
    private final AuthenticationService authenticationService;

    @PostConstruct
    private void init() {
        javalin.post("/gogs/api/auth/register", (ctx) -> {
            AuthRequest request = ctx.bodyAsClass(AuthRequest.class);
            try {
                AuthResponse response = authenticationService.registerUser(request);
                ctx.status(HttpStatus.OK_200).json(response);
            } catch (Exception e) {
                ctx.status(HttpStatus.BAD_REQUEST_400).json(e.getMessage());
            }
        });
    }
}

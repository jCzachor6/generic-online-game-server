package generic.online.game.server.gogs.api.auth;

import generic.online.game.server.gogs.api.auth.model.AuthResponse;
import generic.online.game.server.gogs.api.auth.service.AnonymousUserService;
import io.javalin.Javalin;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@Profile(AnonymousController.PROFILE)
@RequiredArgsConstructor
public class AnonymousController {
    public final static String PROFILE = "AUTH_ANONYMOUS";

    private final Javalin javalin;
    private final AnonymousUserService anonymousUserManager;

    @PostConstruct
    private void init() {
        javalin.post("/gogs/api/auth/anonymous", (ctx) -> {
            AuthResponse response = new AuthResponse(anonymousUserManager.generateUser());
            ctx.status(HttpStatus.CREATED_201).json(response);
        });
    }
}


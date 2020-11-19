package generic.online.game.server.external.impl.user;

import generic.online.game.server.gogs.api.GgsAuthenticationController;
import generic.online.game.server.gogs.api.service.GgsAuthenticationService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends GgsAuthenticationController {
    public UserController(GgsAuthenticationService service) {
        super(service);
    }
}

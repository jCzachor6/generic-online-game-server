package generic.online.game.server.external.impl;

import generic.online.game.server.gogs.api.GgsAuthenticationController;
import generic.online.game.server.gogs.api.GgsAuthenticationService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ComponentScan(basePackages = "czachor.jakub")
public class UserController extends GgsAuthenticationController {

    public UserController(GgsAuthenticationService service) {
        super(service);
    }
}

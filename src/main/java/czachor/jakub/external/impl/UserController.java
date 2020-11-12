package czachor.jakub.external.impl;

import czachor.jakub.ggs.api.GgsAuthenticationController;
import czachor.jakub.ggs.settings.GameUserSettings;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends GgsAuthenticationController {
    public UserController(GameUserSettings gameUserSettings, UserService ggsUserService) {
        super(gameUserSettings, ggsUserService);
    }
}

package generic.online.game.server.gogs.model.auth.model;

import lombok.Value;

@Value
public class AuthResponse {
    String id;
    String username;
    String password;
    String jwt;
}

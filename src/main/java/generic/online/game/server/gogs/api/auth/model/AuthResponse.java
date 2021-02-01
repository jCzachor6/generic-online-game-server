package generic.online.game.server.gogs.api.auth.model;

import lombok.Value;

@Value
public class AuthResponse {
    String id;
    String username;
    String jwt;

    public AuthResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.jwt = user.getToken();
    }
}

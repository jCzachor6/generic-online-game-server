package generic.online.game.server.gogs.api.auth.model;

import java.util.List;

public record User(String token, String id, String username, String password, List<String> roles) {
}


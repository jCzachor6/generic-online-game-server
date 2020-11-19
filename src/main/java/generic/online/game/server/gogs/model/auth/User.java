package generic.online.game.server.gogs.model.auth;

import lombok.*;

import java.util.HashMap;

@Data
public class User {
    private String id;
    private String token;
    private String username;
    private String password;
    private HashMap<String, String> criteria;
}

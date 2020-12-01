package generic.online.game.server.gogs.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;

@Data
public class User {
    private String id;
    @JsonIgnore
    private String token;
    private String username;
    private String password;
}

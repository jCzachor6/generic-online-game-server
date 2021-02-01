package generic.online.game.server.gogs.api.auth.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@FieldNameConstants
@Value
public class User {
    @With
    String token;
    String id;
    String username;
    String password;
    List<String> roles;
}


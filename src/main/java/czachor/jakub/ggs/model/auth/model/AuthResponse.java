package czachor.jakub.ggs.model.auth.model;

import lombok.Value;

@Value
public class AuthResponse {
    String id;
    String username;
    String password;
    String jwt;
}

package czachor.jakub.ggs.model.auth.model;

import lombok.Data;

@Data
public class AuthRequest {
    private String jwt;
    private String username;
    private String password;
    private boolean anonymous;
}

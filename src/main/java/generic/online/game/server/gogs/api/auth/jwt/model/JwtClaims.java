package generic.online.game.server.gogs.api.auth.jwt.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtClaims {
    private String id;
    private String username;
    private List<String> roles;
    private Date exp;

    public static JwtClaims fromJson(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, JwtClaims.class);
    }

    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }
}

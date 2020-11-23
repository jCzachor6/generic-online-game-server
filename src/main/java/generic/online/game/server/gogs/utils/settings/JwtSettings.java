package generic.online.game.server.gogs.utils.settings;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class JwtSettings {
    private final String secret;
    private final long expirationInMs;
    private final SignatureAlgorithm encryptionAlgorithm;
}

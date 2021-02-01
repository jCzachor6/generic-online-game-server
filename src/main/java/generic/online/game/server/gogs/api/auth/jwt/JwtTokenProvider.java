package generic.online.game.server.gogs.api.auth.jwt;

import generic.online.game.server.gogs.GogsConfig;
import generic.online.game.server.gogs.api.auth.model.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final GogsConfig gogsConfig;

    public String generateToken(String id, String username, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + gogsConfig.jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(id)
                .claim(User.Fields.id, id)
                .claim(User.Fields.username, username)
                .claim(User.Fields.roles, StringUtils.join(roles, ","))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(gogsConfig.jwtEncryptionAlgorithm, gogsConfig.jwtSecret)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(gogsConfig.jwtSecret).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String authToken) {
        try {
            getClaims(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException
                | UnsupportedJwtException | IllegalArgumentException ignored) {
            return false;
        }
    }
}

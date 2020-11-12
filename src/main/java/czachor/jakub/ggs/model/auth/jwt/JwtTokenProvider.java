package czachor.jakub.ggs.model.auth.jwt;

import czachor.jakub.ggs.model.auth.model.GgsAuthenticationException;
import czachor.jakub.ggs.settings.JwtSettings;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    public final JwtSettings jwtSettings;;

    public String generateToken(Authentication authentication) throws GgsAuthenticationException {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtSettings.getExpirationInMs());

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .claim("username", userPrincipal.getUsername())
                .claim("id", userPrincipal.getId())
                .claim("role", userPrincipal.getRole())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(jwtSettings.getEncryptionAlgorithm(), jwtSettings.getSecret())
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSettings.getSecret()).parseClaimsJws(token).getBody();
        return claims.get("id", String.class);
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSettings.getSecret()).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSettings.getSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException
                | MalformedJwtException
                | ExpiredJwtException
                | UnsupportedJwtException
                | IllegalArgumentException ignored) {
            return false;
        }
    }
}

package generic.online.game.server.gogs.api.auth.jwt.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import generic.online.game.server.gogs.api.auth.jwt.model.JwtClaims;
import generic.online.game.server.gogs.api.auth.jwt.model.JwtToken;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class JwtHmacSha256AlgorithmImpl implements JwtTokenAlgorithm {

    @Override
    public JwtToken generateToken(String secret, Long expiration, String id, String username, List<String> roles) throws JsonProcessingException {
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        var claims = JwtClaims.builder()
                .id(id)
                .username(username)
                .roles(roles)
                .exp(new Date(System.currentTimeMillis() + expiration))
                .build();
        String encodedHeader = base64UrlEncode(encodeToJson(header));
        String encodedPayload = base64UrlEncode(claims.toJson());

        String signature = generateSignature(secret, encodedHeader, encodedPayload);

        String token = encodedHeader + "." + encodedPayload + "." + signature;
        return new JwtToken(token);
    }

    @Override
    public JwtClaims getClaims(String secret, JwtToken token) {
        try {
            String[] parts = token.value().split("\\.");
            String encodedHeader = parts[0];
            String encodedPayload = parts[1];

            String decodedPayload = base64UrlDecode(encodedPayload);
            JwtClaims claims = JwtClaims.fromJson(decodedPayload);

            String signature = generateSignature(secret, encodedHeader, encodedPayload);
            if (!signature.equals(parts[2])) {
                throw new RuntimeException("Invalid signature");
            }

            return claims;
        } catch (Exception e) {
            throw new RuntimeException("Invalid token", e);
        }
    }

    @Override
    public boolean validateToken(String secret, JwtToken token) {
        try {
            var claims = getClaims(secret, token);
            return claims.getExp().after(new Date());
        } catch (RuntimeException e) {
            return false;
        }

    }

    private String base64UrlEncode(String data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes());
    }

    private String base64UrlDecode(String data) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(data);
        return new String(decodedBytes);
    }

    private String encodeToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        map.forEach((key, value) -> {
            if (json.length() > 1) json.append(",");
            json.append("\"").append(key).append("\":\"").append(value).append("\"");
        });
        json.append("}");
        return json.toString();
    }

    private String generateSignature(String secret, String encodedHeader, String encodedPayload) {
        try {
            String data = encodedHeader + "." + encodedPayload;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((data + secret).getBytes());
            return base64UrlEncode(new String(hash));
        } catch (Exception e) {
            throw new RuntimeException("Error generating signature", e);
        }
    }
}

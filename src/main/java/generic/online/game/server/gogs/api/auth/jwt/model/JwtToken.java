package generic.online.game.server.gogs.api.auth.jwt.model;

public record JwtToken(String value) {
    public boolean isBlank() {
        return value == null || value.trim().isEmpty();
    }
}

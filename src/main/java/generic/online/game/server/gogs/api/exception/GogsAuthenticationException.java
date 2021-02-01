package generic.online.game.server.gogs.api.exception;

public class GogsAuthenticationException extends RuntimeException {
    public GogsAuthenticationException(String message) {
        super(message);
    }
}

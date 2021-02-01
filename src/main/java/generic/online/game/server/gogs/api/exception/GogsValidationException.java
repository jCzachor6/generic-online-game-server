package generic.online.game.server.gogs.api.exception;

public class GogsValidationException extends RuntimeException {
    public GogsValidationException(String message) {
        super(message);
    }
}

package generic.online.game.server.gogs.api;

public class GogsValidationException extends RuntimeException {
    public GogsValidationException(String message) {
        super(message);
    }
}

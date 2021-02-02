package generic.online.game.server.gogs.utils.interfaces;

import generic.online.game.server.gogs.api.auth.model.User;

@FunctionalInterface
public interface MessageHandler {
    void handleMessage(User user, String body);
}

package generic.online.game.server.gogs.utils.interfaces;

import generic.online.game.server.gogs.api.auth.model.User;

public interface OnDisconnect {
    void onDisconnect(User user);
}

package generic.online.game.server.gogs.utils.interfaces;

import generic.online.game.server.gogs.api.auth.model.User;

public interface OnConnect {
    void onConnect(User user);
}

package generic.online.game.server.gogs.impl.rooms.coordinator_room;

import generic.online.game.server.gogs.model.auth.User;

import java.util.Set;

public interface OnGameFound {
    void create(Set<User> users, Object additionalData);
}

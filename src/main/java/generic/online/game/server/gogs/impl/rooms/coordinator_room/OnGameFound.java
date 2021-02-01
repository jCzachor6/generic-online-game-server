package generic.online.game.server.gogs.impl.rooms.coordinator_room;

import generic.online.game.server.gogs.api.auth.model.User;

import java.util.Set;

public interface OnGameFound {
    void create(Set<User> users, Object additionalData);
}

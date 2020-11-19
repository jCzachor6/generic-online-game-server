package generic.online.game.server.gogs.model.rooms;

import generic.online.game.server.gogs.model.auth.User;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class GameRoom {
    private final Set<User> users;
}

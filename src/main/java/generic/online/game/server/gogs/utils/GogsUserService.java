package generic.online.game.server.gogs.utils;

import generic.online.game.server.gogs.api.auth.model.User;

public interface GogsUserService<T, ID> {
    User map(T t);

    User getOneById(ID id);

    User getOneByUsername(String username);

    User createOne(String username, String password);
}

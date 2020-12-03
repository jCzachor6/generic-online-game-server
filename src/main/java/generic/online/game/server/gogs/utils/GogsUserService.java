package generic.online.game.server.gogs.utils;

import generic.online.game.server.gogs.model.auth.User;


public interface GogsUserService<T> {
    User map(T t);

    User getOneByUsername(String username);

    User createOne(String username, String password);

    User editUsername(String id, String username);
}

package generic.online.game.server.gogs.api.service;

import generic.online.game.server.gogs.model.user.User;


public interface GgsUserService<T> {
    User map(T t);

    User getOne(String id);

    User getOneByUsername(String username);

    User createOne(String username, String password);

    User editUsername(String id, String username);

    void removeUser(String id);
}

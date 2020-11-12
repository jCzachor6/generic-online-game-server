package czachor.jakub.ggs.api.service;

import czachor.jakub.ggs.model.user.User;


public interface GgsUserService<T> {
    User map(T t);

    User getOne(String id);

    User createOne(String username, String password);

    User edit(String id, String username, String password);

    void removeUser(String id);
}

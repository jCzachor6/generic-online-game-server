package fixtures;

import generic.online.game.server.gogs.model.auth.User;

import java.util.HashSet;
import java.util.Set;

public class UserFixture {
    public static Set<User> twoUsers() {
        Set<User> users = new HashSet<>();
        users.add(anonUser());
        users.add(danyUser());
        return users;
    }

    public static User anonUser() {
        User user = new User();
        user.setId("1");
        return user;
    }

    public static User danyUser() {
        User user = new User();
        user.setId("2");
        return user;
    }
}

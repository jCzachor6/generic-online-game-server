package fixtures;

import generic.online.game.server.gogs.api.auth.model.User;

import java.util.ArrayList;
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
        return new User("token1", "aab", "1", "user1", new ArrayList<>());
    }

    public static User danyUser() {
        return new User("token2", "aac", "2", "user2", new ArrayList<>());
    }
}

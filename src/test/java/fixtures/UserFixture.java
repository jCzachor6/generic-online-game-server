package fixtures;

import generic.online.game.server.gogs.model.user.User;

public class UserFixture {
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
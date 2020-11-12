package fixtures;

import czachor.jakub.ggs.model.user.User;

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

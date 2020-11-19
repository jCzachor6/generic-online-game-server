package generic.online.game.server.gogs.impl;

import generic.online.game.server.gogs.utils.PasswordGenerator;

import java.util.Date;
import java.util.Random;

public class PinPasswordGenerator implements PasswordGenerator {
    @Override
    public String generate() {
        int min = 1000;
        int max = 9999;
        Random rng = new Random(new Date().getTime());
        int random = rng.nextInt(max - min) + min;
        return Integer.toString(random);
    }
}

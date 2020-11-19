package generic.online.game.server.gogs.impl;

import generic.online.game.server.gogs.utils.RoomIdGenerator;

import java.util.UUID;

public class RoomUuidGenerator implements RoomIdGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

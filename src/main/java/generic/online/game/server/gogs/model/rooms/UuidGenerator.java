package generic.online.game.server.gogs.model.rooms;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@RequiredArgsConstructor
public class UuidGenerator {
    private final int length;

    public String generate() {
        return RandomStringUtils.randomAlphabetic(length);
    }
}

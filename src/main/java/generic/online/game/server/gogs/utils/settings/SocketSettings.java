package generic.online.game.server.gogs.utils.settings;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
public class SocketSettings {
    private final String namespace;
    private final int port;
}

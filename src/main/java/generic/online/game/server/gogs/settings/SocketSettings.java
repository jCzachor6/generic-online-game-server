package generic.online.game.server.gogs.settings;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class SocketSettings {
    private final String namespace;
    private final int port;
}

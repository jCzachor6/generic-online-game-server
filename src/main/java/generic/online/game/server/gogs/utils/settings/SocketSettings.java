package generic.online.game.server.gogs.utils.settings;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
public class SocketSettings {
    private final String namespace;
    private final int port;
    @Setter
    private SocketIOServer server;

    @Builder
    public SocketSettings(String namespace, int port) {
        this.namespace = namespace;
        this.port = port;
    }
}

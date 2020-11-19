package generic.online.game.server.gogs.api;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DefaultExceptionListener;
import generic.online.game.server.gogs.api.service.GgsAuthenticationService;
import generic.online.game.server.gogs.api.service.GgsSocketEventHandler;
import generic.online.game.server.gogs.model.rooms.GameRoom;
import generic.online.game.server.gogs.model.rooms.WaitingRoom;
import generic.online.game.server.gogs.settings.SocketSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class GgsSockets {

    @Bean
    public SocketIOServer socketIOServer(SocketSettings socketSettings,
                                         GgsAuthenticationService authenticationService,
                                         GgsSocketEventHandler ggsSocketEventHandler) {
        Configuration config = new Configuration();
        config.setPort(socketSettings.getPort());
        config.setExceptionListener(new DefaultExceptionListener());
        config.setAuthorizationListener(authenticationService::authenticateUser);
        config.getSocketConfig().setReuseAddress(true);
        config.getSocketConfig().setSoLinger(0);
        config.getSocketConfig().setTcpNoDelay(true);
        config.getSocketConfig().setTcpKeepAlive(true);

        SocketIOServer server = new SocketIOServer(config);
        SocketIONamespace socketIONamespace = server.addNamespace(socketSettings.getNamespace());
        socketIONamespace.addListeners(ggsSocketEventHandler);
        server.start();
        return server;
    }

    @Bean
    public Map<String, SocketIOClient> clientsMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, WaitingRoom> waitingRoomMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, GameRoom> gameRoomMap() {
        return new ConcurrentHashMap<>();
    }
}

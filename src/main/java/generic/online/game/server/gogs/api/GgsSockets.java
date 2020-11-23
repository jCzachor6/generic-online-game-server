package generic.online.game.server.gogs.api;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DefaultExceptionListener;
import generic.online.game.server.gogs.api.service.CoordinatorEventsService;
import generic.online.game.server.gogs.api.service.AuthenticationService;
import generic.online.game.server.gogs.model.rooms.GameRoom;
import generic.online.game.server.gogs.model.rooms.WaitingRoom;
import generic.online.game.server.gogs.utils.settings.SocketSettings;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GgsSockets {

    @Bean
    public SocketIOServer socketIOServer(SocketSettings socketSettings,
                                         AuthenticationService authenticationService,
                                         CoordinatorEventsService coordinatorEventsService) {
        Configuration config = setupConfiguration(socketSettings, authenticationService);
        SocketIOServer server = new SocketIOServer(config);
        SocketIONamespace coordinatorNamespace = server.addNamespace(socketSettings.getNamespace() + "/coordinator");
        coordinatorNamespace.addListeners(coordinatorEventsService);

        socketSettings.setServer(server);
        server.start();
        return server;
    }

    private Configuration setupConfiguration(SocketSettings ss, AuthenticationService authenticationService) {
        Configuration config = new Configuration();
        config.setPort(ss.getPort());
        config.setExceptionListener(new DefaultExceptionListener());
        config.setAuthorizationListener(authenticationService::authenticateUser);
        config.getSocketConfig().setReuseAddress(true);
        config.getSocketConfig().setSoLinger(0);
        config.getSocketConfig().setTcpNoDelay(true);
        config.getSocketConfig().setTcpKeepAlive(true);
        return config;
    }

    @Bean
    public Map<String, SocketIOClient> clientsMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, WaitingRoom> waitingRoomMap() {
        return new ConcurrentHashMap<>();
    }
}

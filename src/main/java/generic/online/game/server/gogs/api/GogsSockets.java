package generic.online.game.server.gogs.api;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DefaultExceptionListener;
import generic.online.game.server.gogs.api.service.AuthenticationService;
import generic.online.game.server.gogs.utils.settings.SocketSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GogsSockets {

    @Bean
    public SocketIOServer socketIOServer(SocketSettings socketSettings,
                                         AuthenticationService authenticationService) {
        Configuration config = setupConfiguration(socketSettings, authenticationService);
        SocketIOServer server = new SocketIOServer(config);
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
}

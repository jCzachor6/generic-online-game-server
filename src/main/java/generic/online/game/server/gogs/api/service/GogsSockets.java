package generic.online.game.server.gogs.api.service;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DefaultExceptionListener;
import generic.online.game.server.gogs.GogsConfig;
import generic.online.game.server.gogs.api.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GogsSockets {
    private final GogsConfig gogsConfig;
    private final AuthenticationService authenticationService;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = setupConfiguration();
        SocketIOServer server = new SocketIOServer(config);
        server.start();
        return server;
    }

    private Configuration setupConfiguration() {
        Configuration config = new Configuration();
        config.setPort(gogsConfig.wsServerPort);
        config.setExceptionListener(new DefaultExceptionListener());
        config.setAuthorizationListener(authenticationService::authenticateUser);
        config.getSocketConfig().setReuseAddress(true);
        config.getSocketConfig().setSoLinger(0);
        config.getSocketConfig().setTcpNoDelay(true);
        config.getSocketConfig().setTcpKeepAlive(true);
        return config;
    }
}

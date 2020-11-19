package generic.online.game.server.gogs.api.service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.auth.jwt.JwtAuthenticationFilter;
import generic.online.game.server.gogs.model.socket.message.coordinator.CoordinatorMessage;
import generic.online.game.server.gogs.model.socket.coordinator.CoordinatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GgsSocketEventHandler {
    private final Map<String, SocketIOClient> clientsMap;
    private final CoordinatorService coordinatorService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @OnConnect
    public void onConnect(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        User user = jwtAuthenticationFilter.getUserFromToken(token);
        client.set("user", user);
        clientsMap.put(token, client);
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        coordinatorService.onCancel(client.get("user"));
        clientsMap.remove(token);
        client.disconnect();
    }

    @OnEvent(value = "coordinator")
    public void onEvent(SocketIOClient client, AckRequest request, CoordinatorMessage message) {
        if (message == null || client.getHandshakeData().getSingleUrlParam("token") == null) {
            return;
        }
        coordinatorService.processMessage(client.get("user"), message);
    }
}

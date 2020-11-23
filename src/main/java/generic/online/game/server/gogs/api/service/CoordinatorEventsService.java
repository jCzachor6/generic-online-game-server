package generic.online.game.server.gogs.api.service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.auth.jwt.JwtAuthenticationFilter;
import generic.online.game.server.gogs.model.socket.coordinator.CoordinatorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CoordinatorEventsService {
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

    @OnEvent(value = "SEARCH")
    public void onSearch(SocketIOClient client, AckRequest request, CoordinatorMessage message) {
        coordinatorService.onSearch(client.get("user"));
    }

    @OnEvent(value = "CANCEL")
    public void onCancel(SocketIOClient client, AckRequest request, CoordinatorMessage message) {
        coordinatorService.onCancel(client.get("user"));
    }

    @OnEvent(value = "ACCEPT")
    public void onAccept(SocketIOClient client, AckRequest request, CoordinatorMessage message) {
        if (message == null || message.getFoundRoomUUID() == null) {
            return;
        }
        coordinatorService.onAccept(client.get("user"), message.getFoundRoomUUID());
    }

    @OnEvent(value = "DECLINE")
    public void onDecline(SocketIOClient client, AckRequest request, CoordinatorMessage message) {
        if (message == null || message.getFoundRoomUUID() == null) {
            return;
        }
        coordinatorService.onDecline(message.getFoundRoomUUID());
    }
}

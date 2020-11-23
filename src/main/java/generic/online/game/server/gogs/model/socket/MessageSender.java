package generic.online.game.server.gogs.model.socket;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MessageSender {
    private final Map<String, SocketIOClient> clientsMap;

    public void send(String from, String to, Message message) {
        clientsMap.get(from).sendEvent(to, message.json());
    }

    public void send(Collection<String> group, String to, Message message) {
        group.forEach(from -> send(from, to, message));
    }

    public void sendBack(String from, Message message) {
        send(from, from, message);
    }

    public void sendBack(Collection<String> group, Message message) {
        group.forEach(from -> sendBack(from, message));
    }
}

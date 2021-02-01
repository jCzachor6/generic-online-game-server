package generic.online.game.server.gogs.model.socket;

import com.corundumstudio.socketio.SocketIOClient;
import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.model.rooms.Room;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class Messenger {
    private final Map<String, SocketIOClient> clientsMap;

    public void send(User from, User to, Message message) {
        send(from.getToken(), to.getToken(), message);
    }

    public void send(User from, Room room, Message message) {
        send(from.getToken(), room.getRoomId(), message);
    }

    public void send(Collection<User> from, Room room, Message message) {
        from.forEach(u -> send(u.getToken(), room.getRoomId(), message));
    }

    public void send(String from, String to, Message message) {
        clientsMap.get(from).sendEvent(to, message.json());
    }

    public void sendBack(Collection<User> from, Message message) {
        from.forEach(u -> send(u.getToken(), u.getToken(), message));
    }

    public void sendToAll(Room room, Message message) {
        room.getOperations().connectedUsers().forEach(u -> send(u.getToken(), room.getRoomId(), message));
    }
}

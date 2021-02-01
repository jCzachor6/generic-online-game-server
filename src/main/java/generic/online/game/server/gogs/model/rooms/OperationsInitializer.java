package generic.online.game.server.gogs.model.rooms;

import com.corundumstudio.socketio.ClientOperations;
import com.corundumstudio.socketio.SocketIOServer;
import generic.online.game.server.gogs.api.auth.model.User;

import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.stream.Collectors;

public interface OperationsInitializer {
    static Operations initialize(RoomInitializerData data, String namespace,
                                 SocketIOServer server, List<Room> rooms) {
        return new Operations() {
            @Override
            public void closeRoom() {
                String roomId = data.getRoomId();
                data.getRoomTimers().forEach(Timer::cancel);
                data.getClientsMap().values().forEach(ClientOperations::disconnect);
                server.removeNamespace(namespace + "/" + roomId);
                rooms.remove(
                        rooms.stream().filter(r -> r.getRoomId().equals(roomId)).findFirst().orElse(null)
                );
            }

            @Override
            public Timer closeRoomAfterTime(int seconds) {
                return new CloseRoomTimer(this).startCounting(seconds);
            }

            @Override
            public Set<User> connectedUsers() {
                return data.getClientsMap().values()
                        .stream()
                        .map(u -> (User) u.get("user"))
                        .collect(Collectors.toSet());
            }
        };
    }
}

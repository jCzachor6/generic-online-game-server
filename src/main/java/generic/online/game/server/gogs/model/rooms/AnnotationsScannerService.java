package generic.online.game.server.gogs.model.rooms;

import com.corundumstudio.socketio.listener.DataListener;
import generic.online.game.server.gogs.api.auth.jwt.JwtAuthenticationFilter;
import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.utils.interfaces.MessageHandler;
import generic.online.game.server.gogs.utils.interfaces.OnConnect;
import generic.online.game.server.gogs.utils.interfaces.OnDisconnect;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Timer;

@Service
@RequiredArgsConstructor
public class AnnotationsScannerService {
    private final RoomValidator validator = new RoomValidator();
    private final JwtAuthenticationFilter authenticationFilter;

    public void setOnTickListeners(AnnotationMethodsParams p) {
        Room room = p.getRoom();
        RoomContext context = new RoomContext();
        room.handlers(context);
        context.getTickHandlerMap().forEach((tickRate, handler) -> {
            validator.validateTickRate(tickRate);
            Timer timer = new TickRateTimer(handler).startTicking(1000 / tickRate);
            p.getRoomTimers().add(timer);
        });
    }

    public void setUserConnectListener(AnnotationMethodsParams p) {
        p.getNamespace().addConnectListener(client -> {
            String token = client.getHandshakeData().getSingleUrlParam("token");
            User user = authenticationFilter.getUser(token);
            if (!validator.validateConnect(user, p)) {
                return;
            }
            client.set("user", user);
            p.getClientsMap().put(token, client);

            Room room = p.getRoom();
            if ((room instanceof OnConnect)) {
                ((OnConnect) room).onConnect(user);
            }
        });
    }

    public void setUserDisconnectListener(AnnotationMethodsParams p) {
        p.getNamespace().addDisconnectListener(client -> {
            String token = client.getHandshakeData().getSingleUrlParam("token");
            User user = authenticationFilter.getUser(token);
            p.getClientsMap().remove(token);
            client.disconnect();
            Room room = p.getRoom();
            if ((room instanceof OnDisconnect)) {
                ((OnDisconnect) room).onDisconnect(user);
            }
        });
    }

    public void setOnMessageListeners(AnnotationMethodsParams p) {
        p.getContext().getMessageHandlerMap().forEach((key, handler) -> {
            String value = p.getEventPrefix() + key;
            p.getNamespace().addEventListener(value, String.class, onMessageDataListener(handler));
        });
    }

    private DataListener<String> onMessageDataListener(MessageHandler handler) {
        return (client, message, ackReq) -> {
            if (message == null || client.getHandshakeData().getSingleUrlParam("token") == null) {
                return;
            }
            handler.handleMessage(client.get("user"), message);
        };
    }

    public void validateRoomParameters(Room room) {
        validator.validateCapacity(room);
    }
}

package generic.online.game.server.gogs.model.rooms;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.listener.DataListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.auth.jwt.JwtAuthenticationFilter;
import generic.online.game.server.gogs.utils.annotations.OnConnect;
import generic.online.game.server.gogs.utils.annotations.OnDisconnect;
import generic.online.game.server.gogs.utils.annotations.OnMessage;
import generic.online.game.server.gogs.utils.annotations.OnTick;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

@RequiredArgsConstructor
public class GameRoomAnnotationsScanner {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SocketIONamespace namespace;
    private GameRoom gameRoom;
    private List<Method> methods;

    public GameRoomAnnotationsScanner setTickRateListener(List<Timer> tickTimers) {
        for (Method m : methods) {
            Optional.ofNullable(m.getAnnotation(OnTick.class)).ifPresent(om -> {
                Timer timer = new TickRateTimer(m, gameRoom).startTicking(1000 / om.tickRate());
                tickTimers.add(timer);
            });
        }
        return this;
    }

    public GameRoomAnnotationsScanner forGameRoom(GameRoom room) {
        gameRoom = room;
        methods = Arrays.asList(gameRoom.getClass().getMethods());
        return this;
    }

    public GameRoomAnnotationsScanner setUserConnectDisconnectListener(
            Map<String, SocketIOClient> clientMap, JwtAuthenticationFilter filter) {
        this.setOnConnectListener(clientMap, filter);
        this.setOnDisconnectListener(clientMap, filter);
        return this;
    }

    public GameRoomAnnotationsScanner setOnEventListeners() {
        for (Method m : methods) {
            Optional.ofNullable(m.getAnnotation(OnMessage.class)).ifPresent(om -> {
                namespace.addEventListener(om.value(), Object.class, onMessageDataListener(m));
            });
        }
        return this;
    }

    private void setOnConnectListener(Map<String, SocketIOClient> clientMap, JwtAuthenticationFilter filter) {
        namespace.addConnectListener(client -> {
            String token = client.getHandshakeData().getSingleUrlParam("token");
            User user = filter.getUserFromToken(token);
            client.set("user", user);
            clientMap.put(token, client);
            methods.stream()
                    .filter(m -> m.getAnnotation(OnConnect.class) != null)
                    .findFirst()
                    .ifPresent(m -> {
                        try {
                            m.invoke(gameRoom, user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        });
    }

    private void setOnDisconnectListener(Map<String, SocketIOClient> clientMap, JwtAuthenticationFilter filter) {
        namespace.addDisconnectListener(client -> {
            String token = client.getHandshakeData().getSingleUrlParam("token");
            User user = filter.getUserFromToken(token);
            clientMap.remove(token);
            client.disconnect();
            methods.stream()
                    .filter(m -> m.getAnnotation(OnDisconnect.class) != null)
                    .findFirst()
                    .ifPresent(m -> {
                        try {
                            m.invoke(gameRoom, user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        });
    }

    private DataListener<Object> onMessageDataListener(Method m) {
        return (client, message, ackReq) -> {
            if (message == null || client.getHandshakeData().getSingleUrlParam("token") == null) {
                return;
            }
            m.invoke(gameRoom, client.get("user"), objectMapper.convertValue(message, messageClass()));
        };
    }

    private Class<?> messageClass() throws ClassNotFoundException {
        String messageType = StringUtils.substringBetween(
                gameRoom.getClass().getGenericSuperclass().getTypeName(), "<", ">");
        return Class.forName(messageType);
    }
}

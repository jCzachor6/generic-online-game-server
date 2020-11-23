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
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class GameRoomAnnotationsScanner {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SocketIONamespace namespace;
    private final GameRoom gameRoom;
    private List<Method> methods;

    public void setListeners(Map<String, SocketIOClient> clientMap, JwtAuthenticationFilter filter) {
        methods = Arrays.asList(gameRoom.getClass().getMethods());
        setOnEventListeners();
        setOnConnectListener(clientMap, filter);
        setOnDisconnectListener(clientMap, filter);
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

    private void setOnEventListeners() {
        for (Method m : methods) {
            Optional.ofNullable(m.getAnnotation(OnMessage.class)).ifPresent(om -> {
                namespace.addEventListener(om.value(), Object.class, onMessageDataListener(m));
            });
        }
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

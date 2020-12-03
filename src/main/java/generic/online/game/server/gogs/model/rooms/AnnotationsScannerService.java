package generic.online.game.server.gogs.model.rooms;

import com.corundumstudio.socketio.listener.DataListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.auth.jwt.JwtAuthenticationFilter;
import generic.online.game.server.gogs.utils.annotations.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Timer;

@Service
@RequiredArgsConstructor
public class AnnotationsScannerService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtAuthenticationFilter authenticationFilter;
    private final AnnotationsValidatorService validatorService;

    public HashMap<Class<? extends Annotation>, List<Method>> gogsAnnotatedMethods(Class<?> room) {
        HashMap<Class<? extends Annotation>, List<Method>> methods = new HashMap<>();

        List<Method> onConnect = MethodUtils.getMethodsListWithAnnotation(room, OnConnect.class);
        methods.put(OnConnect.class, onConnect);
        validatorService.validateOnConnectMethods(onConnect);

        List<Method> onDisconnect = MethodUtils.getMethodsListWithAnnotation(room, OnDisconnect.class);
        methods.put(OnDisconnect.class, onDisconnect);
        validatorService.validateOnDisconnectMethods(onDisconnect);

        List<Method> onMessage = MethodUtils.getMethodsListWithAnnotation(room, OnMessage.class);
        methods.put(OnMessage.class, onMessage);
        validatorService.validateOnMessageMethods(onMessage);

        List<Method> onTick = MethodUtils.getMethodsListWithAnnotation(room, OnTick.class);
        methods.put(OnTick.class, onConnect);
        validatorService.validateOnTickMethods(onTick);

        return methods;
    }

    public void setOnTickListeners(AnnotationMethodsParams p, List<Timer> tickTimers) {
        for (Method m : p.getMethods()) {
            Optional.ofNullable(m.getAnnotation(OnTick.class)).ifPresent(om -> {
                Timer timer = new TickRateTimer(m, p.getRoom()).startTicking(1000 / om.tickRate());
                tickTimers.add(timer);
            });
        }
    }

    public void setUserConnectListener(AnnotationMethodsParams p) {
        p.getNamespace().addConnectListener(client -> {
            String token = client.getHandshakeData().getSingleUrlParam("token");
            User user = authenticationFilter.getUserFromToken(token);
            if (!validatorService.validateConnect(user, p)) {
                return;
            }
            client.set("user", user);
            p.getClientsMap().put(token, client);
            if (p.getMethods().size() == 1) {
                try {
                    p.getMethods().get(0).invoke(p.getRoom(), user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setUserDisconnectListener(AnnotationMethodsParams p) {
        p.getNamespace().addDisconnectListener(client -> {
            String token = client.getHandshakeData().getSingleUrlParam("token");
            User user = authenticationFilter.getUserFromToken(token);
            p.getClientsMap().remove(token);
            client.disconnect();
            if (p.getMethods().size() == 1) {
                try {
                    p.getMethods().get(0).invoke(p.getRoom(), user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setOnMessageListeners(AnnotationMethodsParams p) throws ClassNotFoundException {
        Class msgClass = messageClass(p.getRoom());
        p.getMethods().forEach(method -> {
            String value = method.getAnnotation(OnMessage.class).value();
            p.getNamespace().addEventListener(value, Object.class, onMessageDataListener(method, p.getRoom(), msgClass));
        });
    }

    private DataListener<Object> onMessageDataListener(Method m, Room<?> room, Class msgClass) {
        return (client, message, ackReq) -> {
            if (message == null || client.getHandshakeData().getSingleUrlParam("token") == null) {
                return;
            }
            m.invoke(room, client.get("user"), objectMapper.convertValue(message, msgClass));
        };
    }

    private Class<?> messageClass(Room<?> room) throws ClassNotFoundException {
        String messageType = StringUtils.substringBetween(
                room.getClass().getGenericSuperclass().getTypeName(), "<", ">");
        return Class.forName(messageType);
    }

    public RoomParameters getRoomParameters(Room room) {
        RoomParameters parameters = room.getClass().getAnnotation(RoomParameters.class);
        if (parameters != null) {
            validatorService.validateRoomParameters(parameters, room.getGameUsers().size());
            return parameters;
        }
        return new RoomParameters() {
            @Override
            public int capacity() {
                return -1;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return RoomParameters.class;
            }
        };
    }
}

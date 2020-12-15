package generic.online.game.server.gogs.model.rooms;

import com.corundumstudio.socketio.listener.DataListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.auth.jwt.JwtAuthenticationFilter;
import generic.online.game.server.gogs.utils.annotations.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

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

    public Map<String, Room> gogsInnerRooms(Room room) throws IllegalAccessException {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(room.getClass(), InternalRoom.class);
        validatorService.validateInternalRoomFields(fields);
        Map<String, Room> innerRooms = new HashMap<>();
        for (Field f : fields) {
            f.setAccessible(true);
            String key = f.getAnnotation(InternalRoom.class).prefix();
            validatorService.validateNotEmpty(InternalRoom.class, key);
            Room val = (Room) f.get(room);
            innerRooms.put(key, val);
        }
        return innerRooms;
    }

    public void setOnTickListeners(AnnotationMethodsParams p) {
        for (Method m : p.getMethods()) {
            Optional.ofNullable(m.getAnnotation(OnTick.class)).ifPresent(om -> {
                validatorService.validateTickRate(m, om.tickRate());
                Timer timer = new TickRateTimer(m, p.getRoom()).startTicking(1000 / om.tickRate());
                p.getRoomTimers().add(timer);
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
            p.getOnConnect().forEach((r, m) -> {
                try {
                    m.invoke(r, user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public void setUserDisconnectListener(AnnotationMethodsParams p) {
        p.getNamespace().addDisconnectListener(client -> {
            String token = client.getHandshakeData().getSingleUrlParam("token");
            User user = authenticationFilter.getUserFromToken(token);
            p.getClientsMap().remove(token);
            client.disconnect();
            p.getOnDisconnect().forEach((r, m) -> {
                try {
                    m.invoke(r, user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public void setOnMessageListeners(AnnotationMethodsParams p) {
        p.getMethods().forEach(method -> {
            Class msgClass = method.getParameterTypes()[1];
            String value = p.getEventPrefix() + method.getAnnotation(OnMessage.class).value();
            p.getNamespace().addEventListener(value, msgClass, onMessageDataListener(method, p.getRoom(), msgClass));
        });
    }

    private DataListener<Object> onMessageDataListener(Method m, Room room, Class msgClass) {
        return (client, message, ackReq) -> {
            if (message == null || client.getHandshakeData().getSingleUrlParam("token") == null) {
                return;
            }
            m.invoke(room, client.get("user"), objectMapper.convertValue(message, msgClass));
        };
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

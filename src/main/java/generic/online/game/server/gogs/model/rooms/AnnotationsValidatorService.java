package generic.online.game.server.gogs.model.rooms;

import generic.online.game.server.gogs.api.GogsValidationException;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.socket.Message;
import generic.online.game.server.gogs.utils.annotations.InternalRoom;
import generic.online.game.server.gogs.utils.annotations.OnMessage;
import generic.online.game.server.gogs.utils.annotations.RoomParameters;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static generic.online.game.server.gogs.utils.annotations.RoomParameters.CAPACITY_ANYONE;
import static generic.online.game.server.gogs.utils.annotations.RoomParameters.CAPACITY_PRESET_USERS;

@Service
public class AnnotationsValidatorService {
    public void validateOnConnectMethods(List<Method> methods) {
        if (methods.size() > 1) {
            throw new GogsValidationException("More than one onConnect method. ");
        }
        for (Method method : methods) {
            this.validateReturnParamVoid(method);
            if (method.getParameterTypes().length != 1 || method.getParameterTypes()[0] != User.class) {
                throw new GogsValidationException(method.getName() + " should have only user param. ");
            }
        }
    }

    public void validateOnDisconnectMethods(List<Method> methods) {
        if (methods.size() > 1) {
            throw new GogsValidationException("More than one onDisconnect method. ");
        }
        for (Method method : methods) {
            this.validateReturnParamVoid(method);
            if (method.getParameterTypes().length != 1 || method.getParameterTypes()[0] != User.class) {
                throw new GogsValidationException(method.getName() + " should have only user param. ");
            }
        }
    }

    public void validateOnMessageMethods(List<Method> methods) {
        validateDuplicates(methods);
        for (Method method : methods) {
            this.validateReturnParamVoid(method);
            if (method.getParameterCount() != 2
                    || method.getParameterTypes()[0] != User.class
                    || Message.class.isAssignableFrom(method.getDeclaringClass())
                    || !method.getParameterTypes()[1].getSuperclass().equals(Message.class)) {
                throw new GogsValidationException(method.getName() + " should have only 'user' and 'message' param. ");
            }
        }
    }

    public void validateOnTickMethods(List<Method> methods) {
        for (Method method : methods) {
            this.validateReturnParamVoid(method);
            if (method.getParameterTypes().length != 1 || method.getParameterTypes()[0] != long.class) {
                throw new GogsValidationException(method.getName() + " should have only 'long' param. ");
            }
        }
    }

    private void validateDuplicates(List<Method> methods) {
        Set<String> uniques = new HashSet<>();
        Set<String> duplicates = methods.stream()
                .map(m -> m.getAnnotation(OnMessage.class).value())
                .filter(val -> !uniques.add(val))
                .collect(Collectors.toSet());
        if (duplicates.size() > 0) {
            throw new GogsValidationException(StringUtils.join(duplicates, ", ") + " duplicated. ");
        }
    }

    private void validateReturnParamVoid(Method method) {
        if (!method.getReturnType().equals(void.class)) {
            throw new GogsValidationException(method.getName() + " should return void type. ");
        }
    }

    public void validateRoomParameters(RoomParameters parameters, int usersSize) {
        int capacity = parameters.capacity();
        if (capacity < CAPACITY_ANYONE) {
            throw new GogsValidationException("Wrong capacity input. ");
        }
        if (capacity > CAPACITY_PRESET_USERS && capacity < usersSize) {
            throw new GogsValidationException("Capacity can't be lower than users to join. ");
        }
    }

    public boolean validateConnect(User user, AnnotationMethodsParams p) {
        int capacity = p.getRoomParameters().capacity();
        int gameUsers = p.getRoom().getGameUsers().size();
        int allConnected = p.getRoom().getOperations().connectedUsers().size();
        if (capacity == CAPACITY_ANYONE || p.getRoom().getGameUsers().contains(user)) {
            return true;
        }
        if (capacity == CAPACITY_PRESET_USERS || capacity == gameUsers) {
            return p.getRoom().getGameUsers().contains(user);
        }
        if (allConnected < capacity) {
            Set<User> notConnectedSet = new HashSet<>(p.getRoom().getGameUsers());
            notConnectedSet.removeAll(p.getRoom().getOperations().connectedUsers());
            return allConnected < capacity - notConnectedSet.size();
        }
        return false;
    }

    public void validateInternalRoomFields(List<Field> fields) {
        validateDuplicatedPrefixes(fields);
        for (Field field : fields) {
            if (!Room.class.isAssignableFrom(field.getDeclaringClass())) {
                throw new GogsValidationException(field.getDeclaringClass() + " should extend from Room.class. ");
            }
        }
    }

    public void validateNotEmpty(Class<?> annotation, String string) {
        if (StringUtils.isEmpty(string)) {
            throw new GogsValidationException(annotation.getName() + " has empty value. ");
        }
    }

    private void validateDuplicatedPrefixes(List<Field> fields) {
        Set<String> uniques = new HashSet<>();
        Set<String> duplicates = fields.stream()
                .map(m -> m.getAnnotation(InternalRoom.class).prefix())
                .filter(val -> !uniques.add(val))
                .collect(Collectors.toSet());
        if (duplicates.size() > 0) {
            throw new GogsValidationException(StringUtils.join(duplicates, ", ") + " duplicated. ");
        }
    }

    public void validateTickRate(Method m, int tickRate) {
        if (tickRate < 1) {
            throw new GogsValidationException(m.getName() + " tick rate is lower than zero. ");
        }
    }
}

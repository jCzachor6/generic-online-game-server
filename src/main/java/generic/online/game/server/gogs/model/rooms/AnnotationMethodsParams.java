package generic.online.game.server.gogs.model.rooms;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import generic.online.game.server.gogs.utils.annotations.RoomParameters;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.*;

@Getter
public class AnnotationMethodsParams {
    private final String eventPrefix;
    private final SocketIONamespace namespace;
    private final Room room;
    private final Map<String, SocketIOClient> clientsMap;
    private final RoomParameters roomParameters;
    private final List<Timer> roomTimers;
    private List<Method> methods;
    private final HashMap<Room, Method> onConnect;
    private final HashMap<Room, Method> onDisconnect;

    public AnnotationMethodsParams(RoomInitializerData initializerData,
                                   SocketIONamespace namespace,
                                   Room room,
                                   RoomParameters parameters) {
        this.eventPrefix = "";
        this.namespace = namespace;
        this.room = room;
        this.clientsMap = initializerData.getClientsMap();
        this.roomParameters = parameters;
        this.roomTimers = initializerData.getRoomTimers();
        this.methods = new ArrayList<>();
        this.onConnect = new HashMap<>();
        this.onDisconnect = new HashMap<>();
    }

    public AnnotationMethodsParams(AnnotationMethodsParams copyFrom,
                                   Room innerRoom,
                                   String eventPrefix,
                                   RoomParameters parameters) {
        this.namespace = copyFrom.namespace;
        this.clientsMap = copyFrom.clientsMap;
        this.roomTimers = copyFrom.roomTimers;
        this.eventPrefix = eventPrefix;
        this.room = innerRoom;
        this.roomParameters = parameters;
        this.methods = new ArrayList<>();
        this.onConnect = copyFrom.onConnect;
        this.onDisconnect = copyFrom.onDisconnect;
    }

    public AnnotationMethodsParams withMethods(List<Method> methods) {
        this.methods = methods;
        return this;
    }
}

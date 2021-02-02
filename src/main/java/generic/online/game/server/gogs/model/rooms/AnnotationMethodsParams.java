package generic.online.game.server.gogs.model.rooms;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Timer;

@Getter
public class AnnotationMethodsParams {
    private final String eventPrefix;
    private final SocketIONamespace namespace;
    private final Room room;
    private final Map<String, SocketIOClient> clientsMap;
    private final List<Timer> roomTimers;
    private RoomContext context;

    public AnnotationMethodsParams(RoomInitializerData initializerData,
                                   SocketIONamespace namespace,
                                   Room room) {
        this.eventPrefix = "";
        this.namespace = namespace;
        this.room = room;
        this.clientsMap = initializerData.getClientsMap();
        this.roomTimers = initializerData.getRoomTimers();
        this.context = new RoomContext();
    }

    public AnnotationMethodsParams(AnnotationMethodsParams copyFrom,
                                   Room innerRoom,
                                   String eventPrefix) {
        this.namespace = copyFrom.namespace;
        this.clientsMap = copyFrom.clientsMap;
        this.roomTimers = copyFrom.roomTimers;
        this.eventPrefix = eventPrefix;
        this.room = innerRoom;
        this.context = new RoomContext();
    }
}

package generic.online.game.server.gogs.model.rooms;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import generic.online.game.server.gogs.utils.annotations.RoomParameters;
import lombok.Value;
import lombok.With;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Value
public class AnnotationMethodsParams {
    @With
    List<Method> methods;
    SocketIONamespace namespace;
    Room<?> room;
    Map<String, SocketIOClient> clientsMap;
    RoomParameters roomParameters;
}

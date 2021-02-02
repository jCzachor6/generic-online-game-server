package generic.online.game.server.gogs.model.rooms;

import generic.online.game.server.gogs.utils.interfaces.MessageHandler;
import generic.online.game.server.gogs.utils.interfaces.TickHandler;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class RoomContext {
    private Map<String, MessageHandler> messageHandlerMap = new HashMap<>();
    private Map<Long, TickHandler> tickHandlerMap = new HashMap<>();

    public void onMessage(String context, MessageHandler messageHandler) {
        messageHandlerMap.put(context, messageHandler);
    }

    public void onTick(Long tickRate, TickHandler tickHandler) {
        tickHandlerMap.put(tickRate, tickHandler);
    }
}

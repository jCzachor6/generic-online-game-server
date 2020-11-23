package generic.online.game.server.gogs.utils;

import generic.online.game.server.gogs.model.rooms.GameRoom;
import generic.online.game.server.gogs.model.rooms.GameRoomInitializerData;

public interface GameRoomInitializer {
    GameRoom initialize(GameRoomInitializerData initializerData, Object queueData);
}

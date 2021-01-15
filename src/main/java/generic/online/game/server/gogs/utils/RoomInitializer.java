package generic.online.game.server.gogs.utils;

import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.model.socket.Message;

@FunctionalInterface
public interface RoomInitializer<T> {
    Room initialize(RoomInitializerData initializerData, T additionalData);
}

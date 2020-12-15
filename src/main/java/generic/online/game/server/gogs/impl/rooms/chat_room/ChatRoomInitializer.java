package generic.online.game.server.gogs.impl.rooms.chat_room;

import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.RoomInitializer;

import java.util.ArrayList;

public class ChatRoomInitializer implements RoomInitializer<ChatRoomData> {

    @Override
    public Room initialize(RoomInitializerData initializerData, ChatRoomData additionalData) {
        if (additionalData == null) {
            additionalData = new ChatRoomData(new ArrayList<>(30), 30);
        }
        return new ChatRoom(initializerData, additionalData);
    }
}

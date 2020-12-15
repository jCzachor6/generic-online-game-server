package generic.online.game.server.gogs.impl.rooms.chat_room;

import lombok.Value;

import java.util.List;

@Value
public class ChatRoomData {
    List<ChatMessage> messages;
    int listSize;
}

package generic.online.game.server.gogs.impl.rooms.chatroom;

import lombok.Value;

import java.util.List;

@Value
public class ChatRoomData {
    List<ChatMessage> messages;
    int listSize;
    boolean profanityFilter;
}

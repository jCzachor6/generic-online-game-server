package generic.online.game.server.gogs.impl.rooms.chat_room;

import generic.online.game.server.gogs.model.socket.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ChatMessage extends Message {
    private int id;
    private String from;
    private ChatMessageType type;
    private String content;
    private Date createdOn;
    private Date editedOn;
}

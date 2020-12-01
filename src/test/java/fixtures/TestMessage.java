package fixtures;

import generic.online.game.server.gogs.model.socket.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TestMessage extends Message {
    private String content;
}

package generic.online.game.server.gogs.model.socket.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultMessage extends Message<Void> {
    private String content;
}

package generic.online.game.server.external.impl.game;

import lombok.ToString;

@ToString
public enum TttMessageType {
    //input
    SET_TILE,
    LEAVE,

    //output
    DATA,
    RESULT,
    RESTART,
    CLOSE
}

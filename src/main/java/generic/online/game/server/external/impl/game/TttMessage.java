package generic.online.game.server.external.impl.game;

import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.socket.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TttMessage extends Message<TttMessageType> {
    private List<Character> tiles;
    private User playerX;
    private User playerO;
    private int tileIndex;
    private String userTurn;
    private boolean playerXasksForRestart;
    private boolean playerOasksForRestart;

    public TttMessage setData(TttRoom room, TttMessageType type) {
        setPlayerXasksForRestart(room.isPlayerXasksForRestart());
        setPlayerOasksForRestart(room.isPlayerOasksForRestart());
        setPlayerO(room.getPlayerO());
        setPlayerX(room.getPlayerX());
        setTiles(room.getTiles());
        setUserTurn(room.getUserTurn());
        setType(type);
        return this;
    }
}

package generic.online.game.server.external.impl.game;

import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.rooms.GameRoom;
import generic.online.game.server.gogs.model.rooms.GameRoomInitializerData;
import generic.online.game.server.gogs.utils.annotations.OnConnect;
import generic.online.game.server.gogs.utils.annotations.OnDisconnect;
import generic.online.game.server.gogs.utils.annotations.OnMessage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static generic.online.game.server.external.impl.game.TttMessageType.*;
import static generic.online.game.server.external.impl.game.TttRoomInitializer.EMPTY_ROOM;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@ToString
public class TttRoom extends GameRoom<TttMessage> {
    private List<Character> tiles; //n - none, x, o
    private User playerX;
    private User playerO;
    private boolean playerXasksForRestart;
    private boolean playerOasksForRestart;
    private boolean gameOver;
    private String userTurn;

    public TttRoom(GameRoomInitializerData data) {
        super(data);
    }

    @OnConnect
    public void onConnect(User user) {
        System.out.println(user);
    }

    @OnDisconnect
    public void onDisconnect(User user) {
        System.out.println(user);
    }

    @OnMessage(value = "DATA")
    public void processDataMessage(User user, TttMessage msg) {
        messageSender.send(user.getToken(), getRoomId(), new TttMessage().setData(this, DATA));
    }

    @OnMessage(value = "SET_TILE")
    public void processSetTileMessage(User user, TttMessage msg) {
        if (!gameOver) {
            this.setTile(user, msg.getTileIndex());
            List<String> tokens = gameUsers.stream().map(User::getToken).collect(Collectors.toUnmodifiableList());
            gameOver = checkTiles('x') || checkTiles('o');
            if (gameOver) {
                messageSender.send(tokens, getRoomId(), new TttMessage().setData(this, RESULT));
            } else {
                messageSender.send(tokens, getRoomId(), new TttMessage().setData(this, DATA));
            }
        }
    }

    @OnMessage(value = "LEAVE")
    public void processLeaveMessage(User user, TttMessage msg) {
    }

    @OnMessage(value = "RESTART")
    public void processRestartMessage(User user, TttMessage msg) {
        if (user.getUsername().equals(playerX.getUsername())) {
            playerXasksForRestart = true;
        } else {
            playerOasksForRestart = true;
        }
        if (playerOasksForRestart && playerXasksForRestart) {
            playerXasksForRestart = false;
            playerOasksForRestart = false;
            this.setTiles(Arrays.asList(EMPTY_ROOM));
            messageSender.send(user.getToken(), getRoomId(), new TttMessage().setData(this, RESTART));
        }
    }

    private void setTile(User user, int tileIndex) {
        if (user.getUsername().equals(userTurn) && tiles.get(tileIndex) == 'n') {
            if (playerX.equals(user)) {
                setTile(tileIndex, 'x');
                userTurn = playerO.getUsername();
            } else {
                setTile(tileIndex, 'o');
                userTurn = playerX.getUsername();
            }
        }
    }

    private void setTile(int index, Character turn) {
        List<Character> newTiles = new ArrayList<>(9);
        for (int i = 0; i < tiles.size(); i++) {
            if (i != index) {
                newTiles.add(tiles.get(i));
            } else {
                newTiles.add(turn);
            }
        }
        tiles = newTiles;
    }

    private Boolean checkTiles(char state) {
        //0|1|2
        //3|4|5
        //6|7|8
        return checkLine(0, 1, 2, state)
                || checkLine(3, 4, 5, state)
                || checkLine(6, 7, 8, state)
                || checkLine(0, 3, 6, state)
                || checkLine(1, 4, 7, state)
                || checkLine(2, 5, 8, state)
                || checkLine(0, 4, 8, state)
                || checkLine(2, 4, 6, state);
    }

    private Boolean checkLine(int tile1, int tile2, int tile3, char state) {
        return tiles.get(tile1).equals(state) && tiles.get(tile2).equals(state) && tiles.get(tile3).equals(state);
    }
}

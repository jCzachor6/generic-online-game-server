package generic.online.game.server.external.impl.game;

import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.rooms.GameRoom;
import generic.online.game.server.gogs.model.rooms.GameRoomInitializerData;
import generic.online.game.server.gogs.utils.GameRoomInitializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Set;

@Component
public class TttRoomInitializer implements GameRoomInitializer {
    public static final Character[] EMPTY_ROOM = {'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n'};

    @Override
    public GameRoom initialize(GameRoomInitializerData initializerData, Object queueData) {
        Set<User> users = initializerData.getUsers();
        TttRoom room = new TttRoom(initializerData);
        room.setPlayerX(CollectionUtils.firstElement(users));
        room.setPlayerO(CollectionUtils.lastElement(users));
        room.setTiles(Arrays.asList(EMPTY_ROOM));
        room.setUserTurn(room.getPlayerO().getUsername());
        return room;
    }
}

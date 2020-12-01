package generic.online.game.server.external.impl.game;

import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.RoomInitializer;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Set;

public class TttRoomInitializer implements RoomInitializer {
    public static final Character[] EMPTY_ROOM = {'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n'};

    @Override
    public Room initialize(RoomInitializerData initializerData, Object queueData) {
        Set<User> users = initializerData.getUsers();
        TttRoom room = new TttRoom(initializerData);
        room.setPlayerX(CollectionUtils.firstElement(users));
        room.setPlayerO(CollectionUtils.lastElement(users));
        room.setTiles(Arrays.asList(EMPTY_ROOM));
        room.setUserTurn(room.getPlayerO().getUsername());
        return room;
    }
}

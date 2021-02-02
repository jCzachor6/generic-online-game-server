package generic.online.game.server.gogs.model.rooms;

import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.api.exception.GogsValidationException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static generic.online.game.server.gogs.utils.interfaces.RoomParameters.CAPACITY_ANYONE;
import static generic.online.game.server.gogs.utils.interfaces.RoomParameters.CAPACITY_PRESET_USERS;

@Service
public class RoomValidator {

    public void validateCapacity(Room room) {
        int capacity = room.capacity();
        int usersSize = room.getGameUsers().size();
        if (capacity < CAPACITY_ANYONE) {
            throw new GogsValidationException("Wrong capacity input. ");
        }
        if (capacity > CAPACITY_PRESET_USERS && capacity < usersSize) {
            throw new GogsValidationException("Capacity can't be lower than users to join. ");
        }
    }

    public boolean validateConnect(User user, AnnotationMethodsParams p) {
        int capacity = p.getRoom().capacity();
        int gameUsers = p.getRoom().getGameUsers().size();
        int allConnected = p.getRoom().getOperations().connectedUsers().size();
        if (capacity == CAPACITY_ANYONE || p.getRoom().getGameUsers().contains(user)) {
            return true;
        }
        if (capacity == CAPACITY_PRESET_USERS || capacity == gameUsers) {
            return p.getRoom().getGameUsers().contains(user);
        }
        if (allConnected < capacity) {
            Set<User> notConnectedSet = new HashSet<>(p.getRoom().getGameUsers());
            notConnectedSet.removeAll(p.getRoom().getOperations().connectedUsers());
            return allConnected < capacity - notConnectedSet.size();
        }
        return false;
    }

    public void validateTickRate(long tickRate) {
        if (tickRate < 1) {
            throw new GogsValidationException("Provided tick rate is lower than zero. ");
        }
    }
}

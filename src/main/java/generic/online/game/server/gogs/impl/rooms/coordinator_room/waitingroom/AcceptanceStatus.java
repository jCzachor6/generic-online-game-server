package generic.online.game.server.gogs.impl.rooms.coordinator_room.waitingroom;

import generic.online.game.server.gogs.api.auth.model.User;
import lombok.Getter;

import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.stream.Collectors;

@Getter
public class AcceptanceStatus {
    private final String id;
    private final Timer closeTimer;
    private final Map<User, Boolean> status;
    private final Object additionalData;

    public AcceptanceStatus(String id, Object additionalData, Set<User> users, Timer timer) {
        this.id = id;
        this.additionalData = additionalData;
        this.status = users.stream().collect(Collectors.toMap(x -> x, x -> false));
        this.closeTimer = timer;
    }

    public void accept(User user) {
        status.put(user, true);
    }

    public boolean allAccepted() {
        return status.entrySet().stream().allMatch(entry -> entry.getValue().equals(true));
    }
}

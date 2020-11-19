package generic.online.game.server.gogs.model.rooms;

import generic.online.game.server.gogs.model.auth.User;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.stream.Collectors;

public class WaitingRoom {
    private final Map<User, Boolean> acceptanceStatus;
    private final Timer timer;

    public WaitingRoom(Set<User> users, Timer timer) {
        this.acceptanceStatus = users.stream().collect(Collectors.toMap(x -> x, x -> false));
        this.timer = timer;
    }

    public void accept(User user) {
        this.acceptanceStatus.replace(user, true);
    }

    public boolean allAccepted() {
        return acceptanceStatus.entrySet().stream().allMatch(entry -> entry.getValue().equals(true));
    }

    public boolean noneAccepted() {
        return acceptanceStatus.entrySet().stream().allMatch(entry -> entry.getValue().equals(false));
    }

    public Set<User> getUsers() {
        return new HashSet<>(this.acceptanceStatus.keySet());
    }

    public void stopTimer() {
        timer.cancel();
    }
}

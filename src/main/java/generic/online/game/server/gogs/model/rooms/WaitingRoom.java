package generic.online.game.server.gogs.model.rooms;

import generic.online.game.server.gogs.model.auth.User;

import java.util.*;
import java.util.stream.Collectors;

public class WaitingRoom {
    private final String roomId;
    private final Map<User, Boolean> acceptanceStatus;
    private final Timer timer;
    private final Object additionalData;

    public WaitingRoom(String roomId, Set<User> users, Timer timer, Object additionalData) {
        this.roomId = roomId;
        this.acceptanceStatus = users.stream().collect(Collectors.toMap(x -> x, x -> false));
        this.timer = timer;
        this.additionalData = additionalData;
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

    public void stopTimer() {
        timer.cancel();
    }

    public Set<User> getUsers() {
        return new HashSet<>(this.acceptanceStatus.keySet());
    }

    public List<String> getUsersTokens() {
        return this.acceptanceStatus.keySet().stream().map(User::getToken).collect(Collectors.toUnmodifiableList());
    }

    public Object getAdditionalData() {
        return additionalData;
    }

    public String getRoomId() {
        return roomId;
    }
}

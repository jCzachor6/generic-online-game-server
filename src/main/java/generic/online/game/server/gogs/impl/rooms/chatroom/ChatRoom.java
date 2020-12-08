package generic.online.game.server.gogs.impl.rooms.chatroom;

import generic.online.game.server.gogs.impl.rooms.chatroom.utils.MessageStore;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.annotations.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static generic.online.game.server.gogs.impl.rooms.chatroom.ChatMessageType.*;

@RoomParameters(capacity = -1)
public class ChatRoom extends Room {
    private final MessageStore messageStore;
    private final Map<User, Date> typingStatus;

    public ChatRoom(RoomInitializerData initializerData, ChatRoomData roomData) {
        super(initializerData);
        typingStatus = new HashMap<>();
        messageStore = new MessageStore(roomData.getListSize(), roomData.getMessages());
    }

    @OnConnect
    public void onConnect(User user) {
        typingStatus.put(user, null);
        ChatMessage message = new ChatMessage();
        message.setFrom(user.getUsername());
        message.setType(JOIN);
        message.setCreatedOn(new Date());
        getMessenger().sendToAll(this, message);
    }

    @OnDisconnect
    public void onDisconnect(User user) {
        typingStatus.remove(user);
        ChatMessage message = new ChatMessage();
        message.setFrom(user.getUsername());
        message.setType(LEAVE);
        message.setCreatedOn(new Date());
        getMessenger().sendToAll(this, message);
    }

    @OnTick(tickRate = 1)
    public void onTick(long dt) {
        long currentTime = new Date().getTime();
        Set<User> typingStatusTimedOut = typingStatus.entrySet().stream()
                .filter(status -> status != null && currentTime - status.getValue().getTime() > 3000)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        typingStatusTimedOut.forEach(user -> {
            ChatMessage message = new ChatMessage();
            message.setType(TYPING_STOPPED);
            message.setFrom(user.getUsername());
            getMessenger().sendToAll(this, message);
            typingStatus.replace(user, null);
        });
    }

    @OnMessage("TYPING")
    public void onTyping(User user, ChatMessage message) {
        message.setType(TYPING);
        message.setFrom(user.getUsername());
        typingStatus.replace(user, new Date());
        getMessenger().sendToAll(this, message);
    }

    @OnMessage("ADD")
    public void onAdd(User user, ChatMessage message) {
        getMessenger().sendToAll(this, messageStore.add(user, message));
    }

    @OnMessage("EDIT")
    public void onEdit(User user, ChatMessage message) {
        getMessenger().sendToAll(this, messageStore.edit(user, message));
    }

    @OnMessage("REMOVE")
    public void onRemove(User user, ChatMessage message) {
        getMessenger().sendToAll(this, messageStore.remove(user, message));
    }
}

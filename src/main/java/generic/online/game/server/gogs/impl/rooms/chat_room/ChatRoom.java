package generic.online.game.server.gogs.impl.rooms.chat_room;

import generic.online.game.server.gogs.impl.rooms.chat_room.utils.MessageStore;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.annotations.*;

import java.util.Date;

import static generic.online.game.server.gogs.impl.rooms.chat_room.ChatMessageType.*;

@RoomParameters(capacity = -1)
public class ChatRoom extends Room {
    private final MessageStore messageStore;

    public ChatRoom(RoomInitializerData initializerData, ChatRoomData roomData) {
        super(initializerData);
        messageStore = new MessageStore(roomData.getListSize(), roomData.getMessages());
    }

    @OnConnect
    public void onConnect(User user) {
        ChatMessage message = new ChatMessage();
        message.setFrom(user.getUsername());
        message.setType(JOIN);
        message.setCreatedOn(new Date());
        getMessenger().sendToAll(this, message);
    }

    @OnDisconnect
    public void onDisconnect(User user) {
        ChatMessage message = new ChatMessage();
        message.setFrom(user.getUsername());
        message.setType(LEAVE);
        message.setCreatedOn(new Date());
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

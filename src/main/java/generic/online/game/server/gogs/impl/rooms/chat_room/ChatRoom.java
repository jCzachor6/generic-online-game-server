package generic.online.game.server.gogs.impl.rooms.chat_room;

import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.impl.rooms.chat_room.utils.MessageStorage;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomContext;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.interfaces.OnConnect;
import generic.online.game.server.gogs.utils.interfaces.OnDisconnect;
import generic.online.game.server.gogs.utils.interfaces.RoomParameters;
import io.javalin.plugin.json.JavalinJson;

import java.util.Date;

import static generic.online.game.server.gogs.impl.rooms.chat_room.ChatMessageType.JOIN;
import static generic.online.game.server.gogs.impl.rooms.chat_room.ChatMessageType.LEAVE;

public class ChatRoom extends Room implements OnConnect, OnDisconnect {
    private static final Class<ChatMessage> CHAT_MSG_CLASS = ChatMessage.class;

    private final MessageStorage messageStorage;

    public ChatRoom(RoomInitializerData initializerData, ChatRoomData roomData) {
        super(initializerData);
        messageStorage = new MessageStorage(roomData.getListSize(), roomData.getMessages());
    }

    public void onConnect(User user) {
        ChatMessage message = new ChatMessage();
        message.setFrom(user.getUsername());
        message.setType(JOIN);
        message.setCreatedOn(new Date());
        getMessenger().sendToAll(this, message);
    }

    public void onDisconnect(User user) {
        ChatMessage message = new ChatMessage();
        message.setFrom(user.getUsername());
        message.setType(LEAVE);
        message.setCreatedOn(new Date());
        getMessenger().sendToAll(this, message);
    }

    @Override
    public void handlers(RoomContext ctx) {
        ctx.onMessage("ADD", this::handleAddMessage);
        ctx.onMessage("EDIT", this::handleEditMessage);
        ctx.onMessage("REMOVE", this::handleRemoveMessage);
    }

    private void handleRemoveMessage(User user, String body) {
        ChatMessage msg = messageStorage.remove(user, JavalinJson.fromJson(body, CHAT_MSG_CLASS));
        getMessenger().sendToAll(this, msg);
    }

    private void handleEditMessage(User user, String body) {
        ChatMessage msg = messageStorage.edit(user, JavalinJson.fromJson(body, CHAT_MSG_CLASS));
        getMessenger().sendToAll(this, msg);
    }

    private void handleAddMessage(User user, String body) {
        ChatMessage msg = messageStorage.add(user, JavalinJson.fromJson(body, CHAT_MSG_CLASS));
        getMessenger().sendToAll(this, msg);
    }

    @Override
    public int capacity() {
        return RoomParameters.CAPACITY_ANYONE;
    }
}

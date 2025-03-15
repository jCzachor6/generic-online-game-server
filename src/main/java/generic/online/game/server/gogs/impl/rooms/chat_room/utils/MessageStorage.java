package generic.online.game.server.gogs.impl.rooms.chat_room.utils;

import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.impl.rooms.chat_room.ChatMessage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static generic.online.game.server.gogs.impl.rooms.chat_room.ChatMessageType.*;

public class MessageStorage {
    private final List<ChatMessage> messages;
    private final int listSize;
    private int id;

    public MessageStorage(int maxSize, List<ChatMessage> initialMessages) {
        messages = new ArrayList<>(initialMessages);
        id = messages.stream().max(Comparator.comparingInt(ChatMessage::getId)).map(ChatMessage::getId).orElse(0);
        listSize = maxSize;
    }

    public ChatMessage add(User from, ChatMessage chatMessage) {
        chatMessage.setFrom(from.username());
        chatMessage.setId(id++);
        chatMessage.setCreatedOn(new Date());
        chatMessage.setType(CONTENT);
        if (listSize == messages.size()) {
            messages.remove(messages.size() - 1);
        }
        messages.add(chatMessage);
        return chatMessage;
    }

    public ChatMessage remove(User from, ChatMessage chatMessage) {
        return messages.stream().filter(cm -> cm.getId() == chatMessage.getId())
                .findFirst()
                .map(msg -> {
                    if (msg.getFrom().equals(from.username())) {
                        msg.setType(REMOVE);
                        msg.setContent("");
                    }
                    return msg;
                })
                .orElse(chatMessage);
    }

    public ChatMessage edit(User from, ChatMessage chatMessage) {
        return messages.stream().filter(cm -> cm.getId() == chatMessage.getId())
                .findFirst()
                .map(msg -> {
                    if (msg.getFrom().equals(from.username())) {
                        msg.setType(EDIT);
                        msg.setEditedOn(new Date());
                        msg.setContent(chatMessage.getContent());
                    }
                    return msg;
                })
                .orElse(chatMessage);
    }
}

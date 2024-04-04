package com.example.demo.events;

import com.example.demo.domain.MessageType;
import org.telegram.telegrambots.meta.api.objects.Message;

public class PhotoMessageEvent extends MessageContainerEvent {
    public PhotoMessageEvent(Message message) {
        super(message);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.photo;
    }
}

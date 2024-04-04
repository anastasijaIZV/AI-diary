package com.example.demo.events;

import com.example.demo.domain.MessageType;

public class TextMessageEvent extends MessageContainerEvent {
    public TextMessageEvent(Object source) {
        super(source);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.fromUser;
    }
}

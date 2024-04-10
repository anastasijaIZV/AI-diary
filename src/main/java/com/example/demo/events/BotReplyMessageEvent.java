package com.example.demo.events;

import com.example.demo.domain.MessageType;

public class BotReplyMessageEvent extends MessageContainerEvent {
    public BotReplyMessageEvent(Object source) {
        super(source);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.fromBot;
    }
}

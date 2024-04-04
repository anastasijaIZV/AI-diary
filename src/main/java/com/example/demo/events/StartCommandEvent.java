package com.example.demo.events;

import com.example.demo.domain.MessageType;

public class StartCommandEvent extends MessageContainerEvent {

    public StartCommandEvent(Object source) {
        super(source);
    }

    public  MessageType getMessageType() {
        return MessageType.fromUser;
    }
}

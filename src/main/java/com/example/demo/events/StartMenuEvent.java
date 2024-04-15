package com.example.demo.events;

import com.example.demo.domain.MessageType;

public class StartMenuEvent extends MessageContainerEvent {

    public StartMenuEvent(Object source) {
        super(source);
    }

    public  MessageType getMessageType() {
        return MessageType.system;
    }
}

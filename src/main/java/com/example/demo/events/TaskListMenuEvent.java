package com.example.demo.events;

import com.example.demo.domain.MessageType;

public class TaskListMenuEvent extends MessageContainerEvent {

    public TaskListMenuEvent(Object source) {
        super(source);
    }

    public  MessageType getMessageType() {
        return MessageType.system;
    }
}

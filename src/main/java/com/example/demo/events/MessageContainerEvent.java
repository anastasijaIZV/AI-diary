package com.example.demo.events;

import com.example.demo.domain.MessageType;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
abstract public class MessageContainerEvent extends ApplicationEvent{
    public MessageContainerEvent(Object source) {
        super(source);
    }

    protected Message getMessage() {
        return (Message) getSource();
    }

    public User getFrom() {
        return getMessage().getFrom();
    }

    public long getChatId() {
        return getMessage().getChatId();
    }

    public String getMessageText() {
        return getMessage().getText();
    }

    abstract public MessageType getMessageType();
}

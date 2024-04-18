package com.example.demo.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
abstract public class ChatEvent extends ApplicationEvent {
    protected long chatId;

    public ChatEvent(Object source, long chatId) {
        super(source);

        this.chatId = chatId;
    }
}

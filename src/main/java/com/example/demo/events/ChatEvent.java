package com.example.demo.events;

import com.example.demo.domain.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
abstract public class ChatEvent extends ApplicationEvent {
    protected User user;
    protected long chatId;

    public ChatEvent(
            long chatId,
            User user,
            Object source
    ) {
        super(source);

        this.chatId = chatId;
        this.user = user;
    }
}

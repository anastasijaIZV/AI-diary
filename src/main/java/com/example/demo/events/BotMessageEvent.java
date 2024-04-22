package com.example.demo.events;

import com.example.demo.domain.User;
import com.example.demo.openai.Message;

public class BotMessageEvent extends ChatEvent {
    public BotMessageEvent(long chatId, User user, Object source) {
        super(chatId, user, source);
    }

    public Message getMessage() {
        return (Message) this.getSource();
    }
}

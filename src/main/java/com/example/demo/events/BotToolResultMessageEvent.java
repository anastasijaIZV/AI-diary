package com.example.demo.events;

import com.example.demo.domain.User;
import com.example.demo.openai.Message;

public class BotToolResultMessageEvent extends BotMessageEvent {
    public BotToolResultMessageEvent(long chatId, User user, Message message) {
        super(chatId, user, message);
    }
}

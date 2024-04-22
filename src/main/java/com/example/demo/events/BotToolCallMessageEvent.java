package com.example.demo.events;

import com.example.demo.domain.User;
import com.example.demo.openai.Message;

public class BotToolCallMessageEvent extends BotMessageEvent {
    public BotToolCallMessageEvent(long chatId, User user, Message message) {
        super(chatId, user, message);
    }
}

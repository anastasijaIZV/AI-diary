package com.example.demo.events;

import com.example.demo.domain.User;
import org.telegram.telegrambots.meta.api.objects.Message;

public class TelegramTextMessageEvent extends TelegramMessageEvent {

    public TelegramTextMessageEvent(long chatId, User user, Message message) {
        super(chatId, user, message);
    }
}

package com.example.demo.events;

import com.example.demo.domain.User;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

abstract public class TelegramMessageEvent extends ChatEvent {

    public TelegramMessageEvent(
            long chatId,
            User user,
            Message message
    ){
        super(chatId, user, message);
    }


    public Message getTelegramMessage() {
        return (Message) this.getSource();
    }
}

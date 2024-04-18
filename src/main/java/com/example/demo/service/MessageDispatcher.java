package com.example.demo.service;

import com.example.demo.domain.Record;
import com.example.demo.domain.User;
import com.example.demo.events.MessageContainerEvent;
import com.example.demo.events.PhotoMessageEvent;
import com.example.demo.events.StartMenuEvent;
import com.example.demo.events.TextMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
@PropertySource("application.properties")
public class MessageDispatcher {

    @Value("${bot.greetings}")
    private String greetings;
    @Autowired
    UserService userService;

    @Autowired
    TelegramBotService telegramBotService;

    @Autowired
    ChatGptService chatGptService;

    @Autowired
    RecordService recordService;

    @EventListener
    void onStartMessageReceived(StartMenuEvent event) {
        User user = getUser(event);
        telegramBotService.sendMessage(
                event.getChatId(),
                greetings.formatted(
                        user.getTelegramUser().getFirstName()
                )
        );
    }

    @EventListener
    void onTextMessageReceived(TextMessageEvent event) {
        try {
            chatGptService.makeCompletionRequest(
                    event.getChatId(),
                    getUser(event),
                    event.getMessageText()
            );
        } catch (Exception error) {
            log.error(error.getMessage());
        }
    }

    private User getUser(MessageContainerEvent event) {
        return userService.addOrCreateTelegramUser(event.getFrom());
    }

    @EventListener
    void onPhotoMessageReceived(PhotoMessageEvent event) {
        telegramBotService.sendMessage(
                event.getChatId(),
                "Sorry, i can not understand photo messages, but we are working on it."
        );
    }

    @EventListener
    void onAnyMessage(MessageContainerEvent event) {
        User user = getUser(event);
        Record r = new Record();
        r.setUser(user);
        r.setMessageType(event.getMessageType());
        r.setText(event.getMessageText());
        r.setChatId(event.getChatId());
        recordService.saveRecord(r);
    }
}

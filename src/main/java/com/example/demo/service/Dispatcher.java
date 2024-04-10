package com.example.demo.service;

import com.example.demo.domain.Record;
import com.example.demo.domain.User;
import com.example.demo.events.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Slf4j
public class Dispatcher {
    @Autowired
    UserService userService;

    @Autowired
    TelegramBotService telegramBotService;

    @Autowired
    ChatGptService chatGptService;

    @Autowired
    RecordService recordService;

    @EventListener
    void onStartMessageReceived(StartCommandEvent event) {
        User user = getUser(event);
        telegramBotService.sendMessage(
                event.getChatId(),
                "Hi, "
                        + user.getTelegramUser().getFirstName()
                        + ", nice to meet you!"
        );
    }

    @EventListener
    void onTextMessageReceived(TextMessageEvent event) {
        User user = getUser(event);
        try {
            telegramBotService.sendMessage(
                    event.getChatId(),
                    chatGptService.getOpenaiResponse(
                            getSystemPrompt(user, event.getChatId()),
                            event.getMessageText()
                    )
            );
        } catch (Exception error) {
            log.error(error.getMessage());
        }
    }

    private User getUser(MessageContainerEvent event) {
        return userService.addOrCreateTelegramUser(event.getFrom());
    }

    private String getSystemPrompt(User user, long chatId) {
        return "Make conversation " + user.getTelegramUser().getFirstName()
                + " on language " + user.getTelegramUser().getLanguageCode()
                + ". Message history: " + recordService.getLast10(chatId).stream().map(
                r -> r.getUser().getTelegramUser().getFirstName() + ": "
                        + r.getText()
        ).collect(Collectors.joining("\n"));
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

package com.example.demo.service;

import com.example.demo.domain.MessageType;
import com.example.demo.domain.Record;
import com.example.demo.domain.User;
import com.example.demo.events.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Slf4j
public class Dispatcher {

    @Value("${debug:false}")
    private boolean debugMode;

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
        if (debugMode) {
            telegramBotService.sendMessage(
                    event.getChatId(),
                    "User data saved: "
                            + user.toString()
            );
        }
    }

    @EventListener
    void onTextMessageReceived(TextMessageEvent event) {
        User user = getUser(event);
        try {
            telegramBotService.sendMessage(
                    event.getChatId(),
                    chatGptService.getOpenaiResponse(
                            getSystemPrompt(user),
                            event.getMessageText()
                    )
            );
        } catch (Exception error) {
            if (debugMode) {
                telegramBotService.sendMessage(
                        event.getChatId(),
                        error.toString()
                );
            }
            log.debug(error.toString());
        }
    }

    private User getUser(MessageContainerEvent event) {
        return userService.addOrCreateTelegramUser(event.getFrom());
    }

    private String getSystemPrompt(User user) {
        return "Make connection to user " + user.getTelegramUser().getFirstName()
                + " on language " + user.getTelegramUser().getLanguageCode()
                + " history: " + recordService.getLast10(user).stream().map(
                        r -> (r.getMessageType() == MessageType.fromBot
                                ? "Chat GPT: "
                                : r.getUser().getTelegramUser().getFirstName() + ": ")

                + r.getText()
                ).collect(Collectors.joining(" "));
    }

    @EventListener
    void onPhotoMessageReceived(PhotoMessageEvent event) {
        telegramBotService.sendMessage(
                event.getChatId(),
                "Sorry, i can not understand photo messages, but we working on it."
        );
    }

    @EventListener
    void onAnyMessage(MessageContainerEvent event) {
        log.info("onAnyMessage: " + event.toString());
        User user = getUser(event);
        Record r = new Record();
        r.setUser(user);
        r.setMessageType(event.getMessageType());
        r.setText(event.getMessageText());

        r = recordService.saveRecord(r);

        log.info(r.toString());
    }
}

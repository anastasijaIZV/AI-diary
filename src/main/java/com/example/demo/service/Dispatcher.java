package com.example.demo.service;

import com.example.demo.domain.Record;
import com.example.demo.domain.User;
import com.example.demo.events.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Slf4j
public class Dispatcher {
    public static final String TODO_LIST_PROMPT = """
            When a user's message suggests they want to add a task or tasks to a to-do list, look for key words and
            phrases such as "add to my to-do list," "remind me to," "I need to," or "task." Based on these cues,
            extract the essence of each task, forming a brief description. This description is used to fill in the
            "description" field in a JSON object. Each task is represented as an individual object with the fields
            "type," which always holds the value "task," and "description," containing the brief description of the
            task. These task objects are then assembled into a JSON array. Resulting message should contain only JSON.
            """;
    @Autowired
    UserService userService;

    @Autowired
    TelegramBotService telegramBotService;

    @Autowired
    ChatGptService chatGptService;

    @Autowired
    RecordService recordService;

    @Autowired
    JsonUtilService jsonUtilService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

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
            String openaiResponse = chatGptService.getOpenaiResponse(
                    getSystemPrompt(user, event.getChatId()),
                    event.getMessageText()
            );

            if (jsonUtilService.isJson(openaiResponse)) {

            }

            telegramBotService.sendMessage(
                    event.getChatId(),
                    openaiResponse
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
                + TODO_LIST_PROMPT
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

package com.example.demo.service;

import com.example.demo.domain.Record;
import com.example.demo.domain.User;
import com.example.demo.events.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MessageDispatcher {
    @Value("${prompt.general}")
    public String generalPrompt;

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

    @Autowired
    TaskService taskService;

    @EventListener
    void onStartMessageReceived(StartMenuEvent event) {
        User user = getUser(event);
        telegramBotService.sendMessage(
                event.getChatId(),
                "Hi, "
                        + user.getTelegramUser().getFirstName()
                        + ", nice to meet you!"
        );
    }

    @EventListener
    void onTaskListMessageReceived(TaskListMenuEvent event) {
        User user = getUser(event);
        taskService.getTasksForUser(user);

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
            event.getChatId();
            String openaiResponse = chatGptService.getOpenaiResponse(
                    event.getChatId(),
                    getGeneralPrompt(user),
                    event.getMessageText()
            );

            if (jsonUtilService.isJson(openaiResponse)) {
                jsonUtilService.getListOfCommands(
                        user, event.getChatId(), openaiResponse
                ).forEach(commandDto -> eventPublisher.publishEvent(
                        new BotCommandEvent(commandDto)
                ));
            } else {
                telegramBotService.sendMessage(
                        event.getChatId(),
                        openaiResponse
                );
            }
        } catch (Exception error) {
            log.error(error.getMessage());
        }
    }

    private User getUser(MessageContainerEvent event) {
        return userService.addOrCreateTelegramUser(event.getFrom());
    }

    private String getGeneralPrompt(User user) {
        return generalPrompt.formatted(
                user.getTelegramUser().getFirstName(),
                user.getTelegramUser().getLanguageCode()
        );
    }

    private String getChatMessageHistoryText(long chatId) {
        List<Record> last10 = recordService.getHistoryRecords(chatId);

        return last10.stream().map(
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

package com.example.demo.service;

import com.example.demo.config.BotConfig;
import com.example.demo.domain.User;
import com.example.demo.events.TelegramPhotoMessageEvent;
import com.example.demo.events.TelegramStartMenuEvent;
import com.example.demo.events.TelegramTextMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TelegramBotService extends TelegramLongPollingBot {

    final BotConfig config;
    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    UserService userService;

    public TelegramBotService(BotConfig config) {
        this.config = config;
        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(
                new BotCommand(
                        "/start",
                        "get a welcome message"
                )
        );
        botCommandList.add(
                new BotCommand(
                        "/taskList",
                        "Get TODO list"
                )
        );
        try {
            this.execute(
                    new SetMyCommands(
                            botCommandList,
                            new BotCommandScopeDefault(),
                            null
                    )
            );
        } catch (TelegramApiException e) {
            log.error("Error setting bot command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            long chatId = update.getMessage().getChatId();
            User user = userService.addOrCreateTelegramUser(
                    update.getMessage().getFrom()
            );

            // handle user photo message - not implemented
            if (update.getMessage().hasPhoto()) {
                eventPublisher.publishEvent(
                        new TelegramPhotoMessageEvent(
                                chatId,
                                user,
                                update.getMessage()
                        )
                );
            }

            // handle text message
            if (update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();

                switch (messageText) {
                    case "/start":
                        eventPublisher.publishEvent(
                                new TelegramStartMenuEvent(
                                        chatId,
                                        user,
                                        update.getMessage()
                                )
                        );
                        break;
                    default:
                        eventPublisher.publishEvent(
                                new TelegramTextMessageEvent(
                                        chatId,
                                        user,
                                        update.getMessage()
                                )
                        );
                }
            }
        }
    }

    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}

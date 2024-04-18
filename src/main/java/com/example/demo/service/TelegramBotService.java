package com.example.demo.service;

import com.example.demo.config.BotConfig;
import com.example.demo.events.BotReplyMessageEvent;
import com.example.demo.events.PhotoMessageEvent;
import com.example.demo.events.StartMenuEvent;
import com.example.demo.events.TextMessageEvent;
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
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            eventPublisher.publishEvent(
                    new PhotoMessageEvent(update.getMessage())
            );
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            switch (messageText) {
                case "/start":
                    eventPublisher.publishEvent(
                            new StartMenuEvent(
                                    update.getMessage()
                            )
                    );
                    break;
                default:
                    eventPublisher.publishEvent(
                            new TextMessageEvent(
                                    update.getMessage()
                            )
                    );
            }
        }
    }

    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            eventPublisher.publishEvent(
                    new BotReplyMessageEvent(
                            execute(message)
                    )
            );
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}

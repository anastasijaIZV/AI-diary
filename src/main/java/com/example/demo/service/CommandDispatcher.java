package com.example.demo.service;

import com.example.demo.events.BotTextMessageEvent;
import com.example.demo.events.BotToolCallMessageEvent;
import com.example.demo.openai.Message;
import com.example.demo.openai.executors.ExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CommandDispatcher {
    @Autowired
    TelegramBotService telegramBotService;

    @Autowired
    ExecutorService executorService;

    @Autowired
    ChatGptService chatGptService;

    @EventListener
    public void onBotReplyEvent(BotTextMessageEvent event) {
        telegramBotService.sendMessage(
                event.getChatId(),
                (String) event.getMessage().getContent()
        );
    }

    @EventListener
    public void onBotToolCallEvent(BotToolCallMessageEvent event) {
        executorService.with(
                event.getChatId(),
                event.getUser()
        );

        this.chatGptService.makeCompletionRequest(
                event.getChatId(),
                event.getUser(),
                handleBotToolCallResult(event.getMessage())
        );
    }

    private List<Message> handleBotToolCallResult(Message message) {
        return message.getTool_calls().stream()
                .map(
                        toolCall -> {
                            try {
                                return executorService.build(
                                        toolCall
                                ).execute();
                            } catch (Exception error) {
                                throw new RuntimeException(error);
                            }
                        }
                ).toList();
    }
}

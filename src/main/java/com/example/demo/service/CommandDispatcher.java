package com.example.demo.service;

import com.example.demo.events.BotReplyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommandDispatcher {
    @Autowired
    TelegramBotService telegramBotService;
    @Autowired
    ExecutorService executorService;

    @EventListener
    public void onBotCommandEvent(BotReplyEvent event) {
        executorService.with(
                event.getDto().chatId(),
                event.getDto().user()
        );

        if (event.getDto().choice().isFunction()) {
            event.getDto().choice().getMessage().getTool_calls().stream()
                    .map(
                            toolCall -> {
                                try {
                                    return executorService.build(
                                            toolCall.getFunction()
                                    ).execute();
                                } catch (Exception error) {
                                    throw new RuntimeException(error);
                                }
                            }
                    ).forEach(result -> this.telegramBotService.sendMessage(
                            event.getDto().chatId(),
                            result
                    ));
        } else {
            this.handleBotMessage(
                    event.getDto().chatId(),
                    event.getDto().choice().getMessage().getContent()
            );
        }
    }

    private void handleBotMessage(long chatId, String message) {
        telegramBotService.sendMessage(
                chatId,
                message
        );
    }
}

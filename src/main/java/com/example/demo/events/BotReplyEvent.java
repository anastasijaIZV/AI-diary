package com.example.demo.events;

import com.example.demo.domain.User;
import com.example.demo.message.openai.CompletionResponse;
import org.springframework.context.ApplicationEvent;

public class BotReplyEvent extends ApplicationEvent {
    public BotReplyEvent(DTO source) {
        super(source);
    }

    public DTO getDto() {
        return (DTO) this.getSource();
    }

    public record DTO(
            CompletionResponse.Choice choice,
            User user,
            long chatId
    ) {
    }
}

package com.example.demo.events;

import com.example.demo.domain.AbstractCommandDto;
import org.springframework.context.ApplicationEvent;

public class BotCommandEvent extends ApplicationEvent {
    public BotCommandEvent(AbstractCommandDto source) {
        super(source);
    }

    public AbstractCommandDto getCommandDto() {
        return (AbstractCommandDto) getSource();
    }

    public String getCommandType() {
        return getCommandDto().getType();
    }
}

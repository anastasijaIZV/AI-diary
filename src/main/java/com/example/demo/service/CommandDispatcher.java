package com.example.demo.service;

import com.example.demo.domain.Task;
import com.example.demo.domain.TaskCommandDto;
import com.example.demo.events.BotCommandEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommandDispatcher {
    @Autowired
    private TaskService taskService;

    @EventListener
    public void onBotCommandEvent(BotCommandEvent event) {
        switch (event.getCommandType()) {
            case "task":
                Task t = new Task();
                TaskCommandDto tc = (TaskCommandDto) event.getCommandDto();
                t.setDescription(tc.getDescription());
                t.setUser(tc.getUser());
                t.setChatId(tc.getChatId());
                taskService.saveTask(t);
            default:
                log.info("unknown command: " + event.getCommandType());
        }
    }
}

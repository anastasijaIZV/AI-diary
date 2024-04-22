package com.example.demo.openai.executors;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TodoCompleteExecutor extends Executor {
    public TodoCompleteExecutor() {
        super();
        this.argumentsClass = DTO.class;
    }

    @Override
    protected Object handle(Object arg) {
        DTO dto = (DTO) arg;
        return this.taskService.completeTask(dto.id);
    }

    public record DTO(
            UUID id
    ) {
    }
}

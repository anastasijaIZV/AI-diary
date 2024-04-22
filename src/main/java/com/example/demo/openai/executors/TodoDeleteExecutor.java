package com.example.demo.openai.executors;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TodoDeleteExecutor extends Executor {
    public TodoDeleteExecutor() {
        super();
        this.argumentsClass = DTO.class;
    }

    @Override
    protected Object handle(Object arg) {
        DTO dto = (DTO) arg;

        taskService.deleteTask(dto.id());
        return "Task %s deleted".formatted(dto.id());
    }

    public record DTO(
            UUID id
    ) {
    }
}

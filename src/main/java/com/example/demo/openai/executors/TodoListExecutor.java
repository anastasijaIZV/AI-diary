package com.example.demo.openai.executors;

import org.springframework.stereotype.Component;

@Component
public class TodoListExecutor extends Executor {
    public TodoListExecutor() {
        super();
        this.argumentsClass = DTO.class;
    }

    @Override
    protected Object handle(Object arg) {
        return this.taskService.getTasksForUser(this.user);
    }

    public record DTO(
    ) {
    }
}

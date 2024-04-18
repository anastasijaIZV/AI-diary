package com.example.demo.message.openai.executors;

import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class TodoListExecutor extends Executor {
    public TodoListExecutor() {
        super();
        this.argumentsClass = DTO.class;
    }

    @Override
    protected String handle(Object arg) {
        AtomicInteger index = new AtomicInteger(1);
        return this.taskService.getTasksForUser(this.user).stream().map(
                task -> "%d. %s (id:%s)".formatted(
                        index.getAndIncrement(),
                        task.getDescription(),
                        task.getId()
                )
        ).collect(Collectors.joining("\n"));
    }

    public record DTO(
    ) {
    }
}

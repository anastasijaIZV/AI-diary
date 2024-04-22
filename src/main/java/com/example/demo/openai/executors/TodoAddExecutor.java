package com.example.demo.openai.executors;

import com.example.demo.domain.Task;
import org.springframework.stereotype.Component;

@Component
public class TodoAddExecutor extends Executor {
    public TodoAddExecutor() {
        super();
        this.argumentsClass = DTO.class;
    }

    @Override
    protected Object handle(Object arg) {
        DTO dto = (DTO) arg;

        Task t = new Task();
        t.setDescription(dto.title());
        t.setChatId(chatId);
        t.setUser(user);
        this.taskService.saveTask(t);

        return t;
    }

    public record DTO(
            String title
    ) {
    }
}

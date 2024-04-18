package com.example.demo.message.openai.executors;

import com.example.demo.domain.Task;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TodoAddExecutor extends Executor {
    public TodoAddExecutor() {

        super();
        this.argumentsClass = DTO.class;
    }

    @Override
    protected String handle(Object arg) {
        DTO dto = (DTO) arg;

        Task t = new Task();
        t.setDescription(dto.title());
        t.setChatId(chatId);
        t.setUser(user);
        this.taskService.saveTask(t);

        return "Task %s created".formatted(t.getId());
    }

    public record DTO(
            String title
    ) {
    }
}

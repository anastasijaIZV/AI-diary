package com.example.demo.openai.executors;

import com.example.demo.domain.User;
import com.example.demo.openai.Message;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExecutorService {
    private long chatId;
    private User user;
    @Autowired
    private BeanFactory context;

    public void with(long chatId, User user) {
        this.chatId = chatId;
        this.user = user;
    }

    public Executor build(Message.ToolCall toolCall) {
        Executor executor = switch (toolCall.getFunction().getName()) {
            case "todo_add" -> context.getBean(TodoAddExecutor.class);
            case "todo_delete" -> context.getBean(TodoDeleteExecutor.class);
            case "todo_list" -> context.getBean(TodoListExecutor.class);
            case "todo_complete" -> context.getBean(TodoCompleteExecutor.class);
            default -> throw new RuntimeException("Unknown function: " + toolCall.getFunction().getName());
        };

        executor.setToolCall(toolCall);
        executor.setUser(user);
        executor.setChatId(chatId);

        return executor;
    }
}

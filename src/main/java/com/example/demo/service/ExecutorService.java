package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.message.openai.Message;
import com.example.demo.message.openai.executors.Executor;
import com.example.demo.message.openai.executors.TodoAddExecutor;
import com.example.demo.message.openai.executors.TodoDeleteExecutor;
import com.example.demo.message.openai.executors.TodoListExecutor;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExecutorService {
    private long chatId;
    private User user;

    @Autowired
    private BeanFactory context;

    public void with(
            long chatId,
            User user
    ) {
        this.chatId = chatId;
        this.user = user;
    }

    public Executor build(
            Message.ToolCall.Function function
    ) {
        Executor executor = switch (function.getName()) {
            case "todo_add" -> context.getBean(TodoAddExecutor.class);
            case "todo_delete" -> context.getBean(TodoDeleteExecutor.class);
            case "todo_list" -> context.getBean(TodoListExecutor.class);
            default -> throw new RuntimeException(
                    "Unknown function: "
                    + function.getName()
            );
        };

        executor.setFunction(function);
        executor.setUser(user);
        executor.setChatId(chatId);

        return executor;
    }
}

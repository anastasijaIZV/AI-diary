package com.example.demo.openai.executors;

import com.example.demo.domain.User;
import com.example.demo.openai.Message;
import com.example.demo.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
abstract public class Executor {
    @Setter
    protected long chatId;
    @Setter
    protected User user;
    protected Class<?> argumentsClass;
    @Autowired
    protected TaskService taskService;
    @Setter
    private Message.ToolCall toolCall;

    public Executor() {
    }

    protected final Object buildArguments(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, this.argumentsClass);
    }

    public final Message execute() throws JsonProcessingException {
        log.debug("Execute task: " + this.getClass());

        try {
            Object result = this.handle(
                    this.buildArguments(
                            toolCall.getFunction().getArguments()
                    )
            );
            return new Message(toolCall, result);
        } catch (Exception error) {
            return new Message(
                    toolCall,
                    error.getMessage()
            );
        }
    }

    abstract protected Object handle(Object arg);
}

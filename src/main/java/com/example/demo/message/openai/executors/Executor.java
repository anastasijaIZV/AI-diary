package com.example.demo.message.openai.executors;

import com.example.demo.domain.User;
import com.example.demo.message.openai.Message;
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
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Setter
    protected long chatId;
    @Setter
    protected User user;
    protected Class<?> argumentsClass;

    @Autowired
    protected TaskService taskService;

    @Setter
    private Message.ToolCall.Function function;

    public Executor() {
    }

    protected Object buildArguments(String json) throws JsonProcessingException {
        return this.objectMapper.readValue(json, this.argumentsClass);
    }

    public String execute() throws JsonProcessingException {
        log.info("Execute task: " + this.getClass());
        return this.handle(
                this.buildArguments(
                        this.function.getArguments()
                )
        );
    }

    abstract protected String handle(Object arg);
}

package com.example.demo.service;

import com.example.demo.config.TodoToolsConfig;
import com.example.demo.domain.Record;
import com.example.demo.domain.User;
import com.example.demo.events.BotTextMessageEvent;
import com.example.demo.events.BotToolCallMessageEvent;
import com.example.demo.events.BotToolResultMessageEvent;
import com.example.demo.openai.CompletionRequest;
import com.example.demo.openai.CompletionResponse;
import com.example.demo.openai.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class ChatGptServiceImpl implements ChatGptService {
    @Value("${bot.prompt}")
    public String generalPrompt;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RecordService recordService;

    @Autowired
    TodoToolsConfig todoToolsConfig;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    private String getGeneralPrompt(User user) {
        return generalPrompt.formatted(
                user.getTelegramUser().getFirstName(),
                user.getTelegramUser().getLanguageCode()
        );
    }

    public void makeUserCompletionRequest(long chatId, User user, String messageText) {
        Message message = new Message(
                Message.MessageRole.user,
                messageText
        );

        this.makeCompletionRequest(
                chatId,
                user,
                List.of(message)
        );
    }

    private void makeRequest(long chatId, User user, CompletionRequest request) {
        try {
            log.info(new ObjectMapper().writeValueAsString(request));
        } catch (JsonProcessingException ignored) {

        }
        CompletionResponse response = restTemplate.postForObject(
                "https://api.openai.com/v1/chat/completions",
                request,
                CompletionResponse.class
        );

        assert response != null;
        response.getChoices()
                .stream().map(choice -> choice.isFunction()
                        ? new BotToolCallMessageEvent(chatId, user, choice.getMessage())
                        : new BotTextMessageEvent(chatId, user, choice.getMessage())
                ).forEachOrdered(
                        event -> this.eventPublisher.publishEvent(event)
                );
    }

    @Override
    public void makeCompletionRequest(long chatId, User user, List<Message> messageList) {
        CompletionRequest request = getCompletionRequest(chatId, user);

        request.getMessages().addAll(messageList);

        messageList.forEach(message -> eventPublisher.publishEvent(
                new BotToolResultMessageEvent(chatId, user, message)
        ));

        makeRequest(chatId, user, request);
    }

    private CompletionRequest getCompletionRequest(long chatId, User user) {
        CompletionRequest request = new CompletionRequest();

        // add system prompt
        request.addMessage(
                new Message(
                        Message.MessageRole.system,
                        this.getGeneralPrompt(user)
                )
        );

        // add message history
        recordService.getHistoryRecords(chatId).stream()
                .map(Record::getMessage)
                .forEachOrdered(request::addMessage);

        // add tools section
        request.addTool(todoToolsConfig.buildAddTool());

        request.addTool(todoToolsConfig.buildDeleteTool());

        request.addTool(todoToolsConfig.buildListTool());

        return request;
    }
}

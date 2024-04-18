package com.example.demo.service;

import com.example.demo.config.TodoToolsConfig;
import com.example.demo.domain.Record;
import com.example.demo.domain.User;
import com.example.demo.events.BotReplyEvent;
import com.example.demo.message.openai.CompletionRequest;
import com.example.demo.message.openai.CompletionResponse;
import com.example.demo.message.openai.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public void makeCompletionRequest(
            long chatId,
            User user,
            String message
    ) {
        CompletionRequest request = new CompletionRequest();

        // add system prompt
        request.addMessage(new Message(
                Message.MessageRole.system,
                this.getGeneralPrompt(user)
        ));

        // add message history
        recordService.getHistoryRecords(chatId).stream()
                .filter(Record::isNotSystem)
                .map(
                        record -> {
                            try {
                                return new Message(
                                        record.getChatRole(),
                                        record.getText()
                                );
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                ).forEachOrdered(request::addMessage);

        // add new user message
        request.addMessage(new Message(
                Message.MessageRole.user,
                message
        ));

        // add tools section
        request.addTool(
                todoToolsConfig.buildAddTool()
        );

        request.addTool(
                todoToolsConfig.buildDeleteTool()
        );

        request.addTool(
                todoToolsConfig.buildListTool()
        );

        CompletionResponse response = restTemplate.postForObject(
                "https://api.openai.com/v1/chat/completions",
                request,
                CompletionResponse.class
        );

        assert response != null;
        response.getChoices().stream()
                .map(choice -> new BotReplyEvent(
                        new BotReplyEvent.DTO(
                                choice,
                                user,
                                chatId
                        )
                ))
                .forEachOrdered(choice -> this.eventPublisher.publishEvent(choice));
    }
}

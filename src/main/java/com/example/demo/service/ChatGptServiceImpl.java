package com.example.demo.service;

import com.example.demo.config.TodoToolsConfig;
import com.example.demo.domain.Record;
import com.example.demo.message.CompletionResponse;
import com.example.demo.message.openai.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ChatGptServiceImpl implements ChatGptService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RecordService recordService;

    @Autowired
    TodoToolsConfig todoToolsConfig;

    public String getOpenaiResponse(
            long chatId,
            String system, // todo: inline variable
            String prompt
    ) {
        CompletionRequest request = new CompletionRequest();

        // add system prompt
        request.addMessage(new Message(
                Message.MessageRole.system,
                system
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
                ).forEach(request::addMessage);

        // add new user message
        request.addMessage(new Message(
                Message.MessageRole.user,
                prompt
        ));

        // add tools section
        request.addTool(
                todoToolsConfig.buildAddTool()
        );

        request.addTool(
                todoToolsConfig.buildDeleteTool()
        );

        CompletionResponse response = restTemplate.postForObject(
                "https://api.openai.com/v1/chat/completions",
                request,
                CompletionResponse.class
        );

        log.info(response.toString());

        assert response != null;
        return response.getChoices().get(0).getMessage().getContent();
    }
}

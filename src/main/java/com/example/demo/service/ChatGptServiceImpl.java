package com.example.demo.service;

import com.example.demo.message.ChatCompletionRequest;
import com.example.demo.message.ChatCompletionResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Log
public class ChatGptServiceImpl implements ChatGptService {

    @Autowired
    RestTemplate restTemplate;

    public String getOpenaiResponse(String system, String prompt) {
        log.info(system);
        ChatCompletionRequest chatCompletionRequest = new ChatCompletionRequest(
                "gpt-3.5-turbo",
                system,
                prompt
        );

        ChatCompletionResponse response = restTemplate.postForObject(
                "https://api.openai.com/v1/chat/completions",
                chatCompletionRequest,
                ChatCompletionResponse.class
        );

        assert response != null;
        return response.getChoices().get(0).getMessage().getContent();
    }
}

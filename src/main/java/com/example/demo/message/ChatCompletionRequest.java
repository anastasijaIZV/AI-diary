package com.example.demo.message;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatCompletionRequest {
    private String model;
    private List<ChatMessage> messages;

    public <userMessage> ChatCompletionRequest(
            String model,
            String systemPrompt,
            String userMessage
    ) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new ChatMessage("system", systemPrompt));
        this.messages.add(new ChatMessage("user", userMessage));
    }
}

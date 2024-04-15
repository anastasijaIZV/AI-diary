package com.example.demo.message.openai;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CompletionRequest {
    private String model = "gpt-3.5-turbo";
    private List<Message> messages = new ArrayList<>();
    private List<FunctionTool> tools;

    public void addTool(FunctionTool tool) {
        if (this.tools == null) {
            this.tools = new ArrayList<>();
        }
        this.tools.add(tool);
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }
}

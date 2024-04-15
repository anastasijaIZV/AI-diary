package com.example.demo.message;

import com.example.demo.message.openai.Message;
import lombok.Data;

import java.util.List;

@Data
public class CompletionResponse {
    private List<Choice> choices;
    private String id;
    private String object;

    @Data
    public static class Choice {
        private int index;
        private Message message;
        private String finish_reason;
    }

}

package com.example.demo.message;

import com.example.demo.message.openai.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class CompletionResponse {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private int index;
        private Message message;
        private String finish_reason;
    }

    private String id;
    private String object;

}

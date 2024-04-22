package com.example.demo.openai;

import lombok.Data;

import java.util.List;
import java.util.Objects;

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

        public boolean isFunction() {
            return Objects.equals(
                    this.finish_reason,
                    "tool_calls"
            );
        }
    }
}

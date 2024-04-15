package com.example.demo.message.openai;

import com.example.demo.message.CompletionResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    public Message(MessageRole role, String content) {
        this.role = role;
        this.content = content;
    }

    @Data
    public static class ToolCall {
        private String id;
        private String type;
        private Function function;

        @Data
        public static class Function {
            private String name;
            private String arguments;
        }
    }

    public enum MessageRole {
        user, assistant, system
    }

    private MessageRole role;
    private String content;

    private List<ToolCall> tool_calls;
}

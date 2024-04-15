package com.example.demo.message.openai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionTool {
    String type = "function";
    Function function;

    public FunctionTool(Function function) {
        this.function = function;
    }
}

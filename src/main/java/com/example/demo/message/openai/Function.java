package com.example.demo.message.openai;

import lombok.Data;

@Data
public class Function {
    private final String description;
    private final String name;

    Parameters parameters = new Parameters();

    public Function(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

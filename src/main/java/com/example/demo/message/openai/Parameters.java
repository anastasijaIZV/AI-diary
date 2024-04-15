package com.example.demo.message.openai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parameters {
    String type = "object";
    Map<String, Property> properties = new HashMap<>();
    List<String> required = new ArrayList<>();

    public Parameters(Map<String, Property> properties) {
        this.properties = properties;
    }

    public Parameters(String type) {
        this.type = type;
    }

    public void addProperty(String name, Property property, boolean required) {
        this.properties.put(name, property);
        if (required) {
            this.required.add(name);
        }
    }

    public void addProperty(String name, Property property) {
        addProperty(name, property, false);
    }

    public Property getProperty(String name) {
        return this.properties.get(name);
    }
}

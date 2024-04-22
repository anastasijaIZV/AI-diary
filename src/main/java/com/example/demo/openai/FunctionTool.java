package com.example.demo.openai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionTool {
    String type = "function";
    Function function;

    public FunctionTool(Function function) {
        this.function = function;
    }

    @Data
    public static class Function {
        private final String description;
        private final String name;

        Parameters parameters = new Parameters();

        public Function(String name, String description) {
            this.name = name;
            this.description = description;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Parameters {
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

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Property {
                String type;
                String description;
            }
        }
    }
}

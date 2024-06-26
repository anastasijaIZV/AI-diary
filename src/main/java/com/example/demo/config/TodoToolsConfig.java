package com.example.demo.config;

import com.example.demo.openai.FunctionTool;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Configuration
@PropertySource("application.properties")
public class TodoToolsConfig {

    @Value("${todo.add.name}")
    private String addName;

    @Value("${todo.add.description}")
    private String addDescription;

    @Value("${todo.add.property.title.name}")
    private String addPropertyTitleName;

    @Value("${todo.add.property.title.description}")
    private String addPropertyTitleDescription;

    @Value("${todo.add.property.title.required}")
    private boolean addPropertyTitleRequired;

    @Value("${todo.delete.name}")
    private String deleteName;

    @Value("${todo.delete.description}")
    private String deleteDescription;

    @Value("${todo.delete.property.id.name}")
    private String deletePropertyIdName;

    @Value("${todo.delete.property.id.description}")
    private String deletePropertyIdDescription;

    @Value("${todo.delete.property.id.required}")
    private boolean deletePropertyIdRequired;

    @Value("${todo.list.name}")
    private String listName;

    @Value("${todo.list.description}")
    private String listDescription;

    public FunctionTool buildAddTool() {
        FunctionTool tool = new FunctionTool(
                new FunctionTool.Function(
                        addName,
                        addDescription
                )
        );

        tool.getFunction().getParameters().addProperty(
                addPropertyTitleName,
                new FunctionTool.Function.Parameters.Property("string", addPropertyTitleDescription),
                addPropertyTitleRequired
        );

        return tool;
    }

    public FunctionTool buildDeleteTool() {
        FunctionTool tool = new FunctionTool(
                new FunctionTool.Function(
                        deleteName,
                        deleteDescription
                )
        );

        tool.getFunction().getParameters().addProperty(
                deletePropertyIdName,
                new FunctionTool.Function.Parameters.Property(
                        "string",
                        deletePropertyIdDescription
                ),
                deletePropertyIdRequired
        );

        return tool;
    }

    public FunctionTool buildListTool() {
        return new FunctionTool(
                new FunctionTool.Function(
                        listName,
                        listDescription
                )
        );
    }
}
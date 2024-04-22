package com.example.demo.service;

import com.example.demo.openai.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

@Converter
public class MessageConverter implements AttributeConverter<Message, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Message attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Message to JSON", e);
        }
    }

    @Override
    public Message convertToEntityAttribute(String data) {
        try {
            return objectMapper.readValue(data, Message.class);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to Message", e);
        }
    }
}

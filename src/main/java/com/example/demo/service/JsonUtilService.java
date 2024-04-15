package com.example.demo.service;

import com.example.demo.domain.AbstractCommandDto;
import com.example.demo.domain.TaskCommandDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class JsonUtilService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean isJson(String content) {
        try {
            objectMapper.readTree(content);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    public JsonNode getJson(String content) throws JsonProcessingException {
        return objectMapper.readTree(content);
    }

    public List<AbstractCommandDto> getListOfCommands(JsonNode node) {
        List<AbstractCommandDto> = new <AbstractCommandDto>();
        while (node.elements().hasNext()) {
            getDtoFromNode(node.elements().next());
        }
    }

    public AbstractCommandDto getDtoFromNode (JsonNode node) {
        return new TaskCommandDto(
                node.get("type").asText(),
                node.get("description").asText()
        );
    }
}

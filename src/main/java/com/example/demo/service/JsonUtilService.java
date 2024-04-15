package com.example.demo.service;

import com.example.demo.domain.AbstractCommandDto;
import com.example.demo.domain.TaskCommandDto;
import com.example.demo.domain.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
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

    public List<AbstractCommandDto> getListOfCommands(
            User user, long chatId, String content
    ) throws JsonProcessingException {
        JsonNode node = getJson(content);
        List<AbstractCommandDto> result = new ArrayList<>();
        Iterator<JsonNode> elements = node.elements();

        if (node.isArray()) {
            while (elements.hasNext()) {
                result.add(
                        getDtoFromNode(
                                elements.next(),
                                user,
                                chatId
                        )
                );
            }
        } else {
            result.add(
                    getDtoFromNode(
                            node,
                            user,
                            chatId
                    )
            );
        }

        log.info(result.toString());

        return result;
    }

    public AbstractCommandDto getDtoFromNode(JsonNode node, User user, long chatId) {
        return new TaskCommandDto(
                user,
                node.get("description").asText(),
                chatId
        );
    }
}

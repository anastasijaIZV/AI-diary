package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public interface ChatGptService {
    String getOpenaiResponse(String system, String prompt);
}

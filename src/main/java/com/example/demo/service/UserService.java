package com.example.demo.service;

import com.example.demo.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User addOrCreateTelegramUser(org.telegram.telegrambots.meta.api.objects.User telegramUser);
}

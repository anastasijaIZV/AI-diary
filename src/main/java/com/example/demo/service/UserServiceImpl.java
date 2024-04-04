package com.example.demo.service;

import com.example.demo.domain.TelegramUser;
import com.example.demo.domain.User;
import com.example.demo.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;


    public User addOrCreateTelegramUser(org.telegram.telegrambots.meta.api.objects.User telegramUser) {
        return userRepository.findByTelegramUser_Id(
                telegramUser.getId()
        ).orElseGet(() -> userRepository.save(
                new User(null,
                        new TelegramUser(
                                telegramUser.getId(),
                                telegramUser.getFirstName(),
                                telegramUser.getLastName(),
                                telegramUser.getUserName(),
                                telegramUser.getLanguageCode()
                        )
                )
        ));
    }
}

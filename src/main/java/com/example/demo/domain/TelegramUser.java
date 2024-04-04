package com.example.demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelegramUser {
    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private String userName;

    private String languageCode;
}

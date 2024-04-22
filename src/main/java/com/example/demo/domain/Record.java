package com.example.demo.domain;

import com.example.demo.openai.Message;
import com.example.demo.service.MessageConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Record {
    @GeneratedValue
    @Id
    private UUID id;

    private long chatId;

    @ManyToOne
    private User user;

    private Message.MessageRole messageRole;

    @CreationTimestamp
    private LocalDateTime created;

    @Convert(converter = MessageConverter.class)
    @Lob
    private Message message;

    public Record(long chatId, User user, Message message) {
        this.chatId = chatId;
        this.user = user;
        this.message = message;
        this.messageRole = message.getRole();
    }
}

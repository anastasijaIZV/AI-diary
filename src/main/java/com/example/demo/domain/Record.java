package com.example.demo.domain;

import com.example.demo.message.openai.Message;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    private MessageType messageType;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    @Lob
    private String text;

    public boolean isNotSystem() {
        return this.messageType != MessageType.system;
    }

    public Message.MessageRole getChatRole() throws Exception {
        switch (messageType) {
            case fromBot -> {
                return Message.MessageRole.assistant;
            }
            case fromUser -> {
                return Message.MessageRole.user;
            }
            default -> throw new Exception("Incorrect message type");
        }
    }
}

package com.example.demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecordRepository extends JpaRepository<Record, UUID> {
    List<Record> findTop20ByChatIdAndMessageTypeNotOrderByCreatedDesc(
            long chatId,
            MessageType notMessageType
    );
}

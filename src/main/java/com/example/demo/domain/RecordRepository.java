package com.example.demo.domain;

import com.example.demo.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Repository
public interface RecordRepository extends JpaRepository<Record, UUID> {
    List<Record> findTop10ByUser_IdOrderByCreatedDesc(UUID userId);
}

package com.example.demo.service;

import com.example.demo.domain.Record;
import com.example.demo.domain.RecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordRepository recordRepository;

    public Record saveRecord(Record record) {
        log.info(record.toString());
        return recordRepository.save(record);
    }

    @Override
    public Page<Record> getPagedRecords(Pageable pageable) {
        return recordRepository.findAll(pageable);
    }

    public List<Record> getHistoryRecords(long chatId) {
        LocalDate today = LocalDate.now();

        return recordRepository.findByChatIdAndCreatedBetweenOrderByCreatedAsc(
                chatId,
                today.atStartOfDay(),
                LocalDateTime.of(today, LocalTime.MAX)
        );
    }
}





















































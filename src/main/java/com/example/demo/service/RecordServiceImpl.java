package com.example.demo.service;

import com.example.demo.domain.MessageType;
import com.example.demo.domain.RecordRepository;
import com.example.demo.domain.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordRepository recordRepository;

    public Record saveRecord(Record record) {
        return recordRepository.save(record);
    }

    @Override
    public Page<Record> getPagedRecords(Pageable pageable) {
        return recordRepository.findAll(pageable);
    }

    public List<Record> getHistoryRecords(long chatId) {
        List<Record> list = recordRepository.findTop20ByChatIdAndMessageTypeNotOrderByCreatedDesc(
                chatId,
                MessageType.system
        );
        Collections.reverse(list);
        return list;
    }
}





















































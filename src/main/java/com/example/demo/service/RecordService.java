package com.example.demo.service;

import com.example.demo.domain.Record;
import com.example.demo.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecordService {
    Record saveRecord(Record record);

    Page<Record> getPagedRecords(Pageable pageable);

    public List<Record> getLast10(User user);
}

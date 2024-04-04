package com.example.demo.service;

import com.example.demo.domain.RecordRepository;
import com.example.demo.domain.Record;
import com.example.demo.domain.User;
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

    public List<Record> getLast10(User user) {
        List<Record> list = recordRepository.findTop10ByUser_IdOrderByCreatedDesc(user.getId());
        Collections.reverse(list);
        return list;
    }
//    @PostConstruct
//    public void init() {
//        for (int i = 0; i < 10; i++) {
//            Record r = new Record();
//            r.setText("test text here " + i);
//            saveRecord(r);
//        }
//    }
}





















































package com.example.demo;

import com.example.demo.domain.Record;
import com.example.demo.service.RecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/record")
public class RecordController {
    @Autowired
    private RecordService recordService;

    @PostMapping("")
    public ResponseEntity<Record> saveRecord(
            @Valid @RequestBody Record record) {

        return ResponseEntity.ok(
                recordService.saveRecord(record)
        );
    }

    @GetMapping("")
    public ResponseEntity<Page<Record>> getAllRecords(Pageable pageable) {
        Page<Record> recordsPage = recordService.getPagedRecords(pageable);
        return ResponseEntity.ok(recordsPage);
    }
}

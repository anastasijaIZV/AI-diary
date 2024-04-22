package com.example.demo.service;

import com.example.demo.domain.Record;
import com.example.demo.events.BotMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class HistoryDispatcher {
    @Autowired
    RecordService recordService;

    @EventListener
    @Order(1)
    public void onAnyBotMessage(BotMessageEvent event) {
        Record r = new Record();
        r.setUser(event.getUser());
        r.setChatId(event.getChatId());
        r.setMessage(event.getMessage());
        r.setMessageRole(event.getMessage().getRole());

        recordService.saveRecord(r);
    }
}

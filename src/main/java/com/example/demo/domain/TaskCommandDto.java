package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class TaskCommandDto extends AbstractCommandDto {
    User user;
    String description;
    private long chatId;

    public String getType() {
        return "task";
    }
}

package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class TaskCommandDto extends AbstractCommandDto {
    User user;
    private long chatId;
    String description;

    public String getType() {
        return "task";
    }
}

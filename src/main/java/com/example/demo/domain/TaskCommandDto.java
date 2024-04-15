package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class TaskCommandDto extends AbstractCommandDto {
    String description;

    public TaskCommandDto(String type, String description) {
        super(type);
        this.setDescription(description);
    }
}

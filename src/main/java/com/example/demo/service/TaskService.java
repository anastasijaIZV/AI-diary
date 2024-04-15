package com.example.demo.service;

import com.example.demo.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Task saveTask(Task task);

    Page<Task> getPagedTasks(Pageable pageable);
}

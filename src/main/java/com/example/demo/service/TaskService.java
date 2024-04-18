package com.example.demo.service;

import com.example.demo.domain.Task;
import com.example.demo.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    void saveTask(Task task);

    Page<Task> getPagedTasks(Pageable pageable);

    List<Task> getTasksForUser(User user);

    void deleteTask(UUID id);
}

package com.example.demo.service;

import com.example.demo.domain.Task;
import com.example.demo.domain.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService{
    @Autowired
    private TaskRepository taskRepository;

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public Page<Task> getPagedTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

}

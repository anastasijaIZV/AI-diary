package com.example.demo.service;

import com.example.demo.domain.Task;
import com.example.demo.domain.TaskRepository;
import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    public Page<Task> getPagedTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public List<Task> getTasksForUser(User user) {
        return taskRepository.findByUserId(user.getId());
    }

    @Override
    public void deleteTask(UUID id) {
        this.taskRepository.deleteById(id);
    }

    @Override
    public Task completeTask(UUID id) {
        Task t = taskRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Task with id:%s not found".formatted(id))
        );

        t.setCompleted(true);

        return taskRepository.save(t);
    }
}

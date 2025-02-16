package com.example.processserver.controller;

import com.example.processserver.Task;
import com.example.processserver.TaskRepository;
import com.example.processserver.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class BatchController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        taskService.submitTask(task);
        return task;
    }

    @GetMapping("/{id}")
    public Task getTaskStatus(@PathVariable Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @GetMapping("/status")
    public List<Task> getAllTaskStatus() {
        return taskRepository.findAll();
    }
}

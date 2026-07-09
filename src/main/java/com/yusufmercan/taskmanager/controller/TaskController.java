package com.yusufmercan.taskmanager.controller;

import com.yusufmercan.taskmanager.dto.TaskRequest;
import com.yusufmercan.taskmanager.dto.TaskResponse;
import com.yusufmercan.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @PostMapping
    public TaskResponse createTask(
            @Valid @RequestBody TaskRequest request
    ) {
        return taskService.createTask(request);
    }


    @GetMapping
    public List<TaskResponse> findAll() {
        return taskService.findAll();
    }


    @GetMapping("/{id}")
    public TaskResponse findById(
            @PathVariable Long id
    ) {
        return taskService.findById(id);
    }


    @PutMapping("/{id}")
    public TaskResponse updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request
    ) {
        return taskService.updateTask(id, request);
    }


    @DeleteMapping("/{id}")
    public void deleteTask(
            @PathVariable Long id
    ) {
        taskService.deleteTaskById(id);
    }
}

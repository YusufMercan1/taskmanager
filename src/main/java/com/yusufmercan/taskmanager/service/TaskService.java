package com.yusufmercan.taskmanager.service;

import com.yusufmercan.taskmanager.dto.TaskRequest;
import com.yusufmercan.taskmanager.dto.TaskResponse;
import com.yusufmercan.taskmanager.entity.Task;
import com.yusufmercan.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    public TaskResponse createTask(TaskRequest request) {

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());

        Task savedTask = taskRepository.save(task);

        return mapToResponse(savedTask);
    }


    public List<TaskResponse> findAll() {

        return taskRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    public TaskResponse findById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        return mapToResponse(task);
    }


    public TaskResponse updateTask(Long id, TaskRequest request) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        existingTask.setTitle(request.getTitle());
        existingTask.setDescription(request.getDescription());
        existingTask.setCompleted(request.isCompleted());

        Task savedTask = taskRepository.save(existingTask);

        return mapToResponse(savedTask);
    }


    public void deleteTaskById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.delete(task);
    }


    private TaskResponse mapToResponse(Task task) {

        TaskResponse response = new TaskResponse();

        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setCompleted(task.isCompleted());

        return response;
    }
}

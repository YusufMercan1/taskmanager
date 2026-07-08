package com.yusufmercan.taskmanager.service;

import com.yusufmercan.taskmanager.entity.Task;
import com.yusufmercan.taskmanager.entity.User;
import com.yusufmercan.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    public Task createTask (Task task) {
        return taskRepository.save(task);
    }
    public List<Task> findAllByUser(User user) {
        return taskRepository.findAll();

    }
    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

}

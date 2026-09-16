package com.yusufmercan.taskmanager.service;

import com.yusufmercan.taskmanager.dto.TaskRequest;
import com.yusufmercan.taskmanager.dto.TaskResponse;
import com.yusufmercan.taskmanager.entity.Task;
import com.yusufmercan.taskmanager.entity.User;
import com.yusufmercan.taskmanager.exception.ResourceNotFoundException;
import com.yusufmercan.taskmanager.repository.TaskRepository;
import com.yusufmercan.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User user;

    @BeforeEach
    void setUp() {

        user = new User();

        user.setId(1L);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@test.com");
        user.setPassword("password");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "test@test.com",
                        null,
                        List.of()
                )
        );
    }

    @Test
    void createTask_shouldCreateTaskForCurrentUser() {

        TaskRequest request = new TaskRequest();

        request.setTitle("Test task");
        request.setDescription("Test description");
        request.setCompleted(false);

        Task savedTask = new Task();

        savedTask.setId(1L);
        savedTask.setTitle("Test task");
        savedTask.setDescription("Test description");
        savedTask.setCompleted(false);
        savedTask.setUser(user);

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        when(taskRepository.save(any(Task.class)))
                .thenReturn(savedTask);

        TaskResponse response = taskService.createTask(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test task", response.getTitle());
        assertEquals("Test description", response.getDescription());
        assertFalse(response.isCompleted());

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void findAll_shouldReturnCurrentUsersTasks() {

        Task task = new Task();

        task.setId(1L);
        task.setTitle("My task");
        task.setDescription("My description");
        task.setCompleted(false);
        task.setUser(user);

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        when(taskRepository.findByUser(user))
                .thenReturn(List.of(task));

        List<TaskResponse> result = taskService.findAll();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("My task", result.get(0).getTitle());

        verify(taskRepository).findByUser(user);
    }

    @Test
    void findById_shouldReturnTaskWhenUserOwnsTask() {

        Task task = new Task();

        task.setId(1L);
        task.setTitle("My task");
        task.setDescription("My description");
        task.setCompleted(false);
        task.setUser(user);

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        TaskResponse response = taskService.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("My task", response.getTitle());
    }

    @Test
    void findById_shouldThrowExceptionWhenTaskDoesNotExist() {

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        when(taskRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.findById(999L)
        );
    }

    @Test
    void findById_shouldThrowExceptionWhenUserDoesNotOwnTask() {

        User anotherUser = new User();
        anotherUser.setId(2L);

        Task task = new Task();

        task.setId(1L);
        task.setTitle("Private task");
        task.setUser(anotherUser);

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.findById(1L)
        );

        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void deleteTaskById_shouldDeleteTaskWhenUserOwnsTask() {

        Task task = new Task();

        task.setId(1L);
        task.setTitle("Delete me");
        task.setUser(user);

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        taskService.deleteTaskById(1L);

        verify(taskRepository).delete(task);
    }
}
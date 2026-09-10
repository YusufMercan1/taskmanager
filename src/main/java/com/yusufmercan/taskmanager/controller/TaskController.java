package com.yusufmercan.taskmanager.controller;

import com.yusufmercan.taskmanager.dto.TaskRequest;
import com.yusufmercan.taskmanager.dto.TaskResponse;
import com.yusufmercan.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(
        name = "Tasks",
        description = "Endpoints for managing tasks"
)
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @PostMapping
    @Operation(
            summary = "Create a task",
            description = "Creates a new task for the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Task created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid task data"
            )
    })
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request
    ) {

        TaskResponse response = taskService.createTask(request);

        return ResponseEntity
                .created(URI.create("/api/tasks/" + response.getId()))
                .body(response);
    }


    @GetMapping
    @Operation(
            summary = "Get all tasks",
            description = "Returns all tasks belonging to the authenticated user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Tasks retrieved successfully"
    )
    public ResponseEntity<List<TaskResponse>> findAll() {

        return ResponseEntity.ok(
                taskService.findAll()
        );
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Get task by ID",
            description = "Returns a specific task belonging to the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found"
            )
    })
    public ResponseEntity<TaskResponse> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                taskService.findById(id)
        );
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Update a task",
            description = "Updates a task belonging to the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid task data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found"
            )
    })
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request
    ) {

        return ResponseEntity.ok(
                taskService.updateTask(id, request)
        );
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a task",
            description = "Deletes a task belonging to the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Task deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found"
            )
    })
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id
    ) {

        taskService.deleteTaskById(id);

        return ResponseEntity.noContent().build();
    }
}
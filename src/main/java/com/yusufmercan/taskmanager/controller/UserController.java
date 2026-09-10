package com.yusufmercan.taskmanager.controller;

import com.yusufmercan.taskmanager.dto.UserRequest;
import com.yusufmercan.taskmanager.dto.UserResponse;
import com.yusufmercan.taskmanager.service.UserService;
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
@RequestMapping("/api/users")
@Tag(
        name = "Users",
        description = "Endpoints for managing users"
)
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    @Operation(
            summary = "Create a user",
            description = "Creates a new user account"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid user data"
            )
    })
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRequest request
    ) {

        UserResponse response = userService.createUser(request);

        return ResponseEntity
                .created(URI.create("/api/users/" + response.getId()))
                .body(response);
    }


    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Returns all registered users"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Users retrieved successfully"
    )
    public ResponseEntity<List<UserResponse>> findAll() {

        return ResponseEntity.ok(
                userService.findAll()
        );
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Returns a specific user by their ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    public ResponseEntity<UserResponse> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                userService.findById(id)
        );
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a user",
            description = "Deletes a user by their ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "User deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id
    ) {

        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }
}
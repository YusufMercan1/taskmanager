package com.yusufmercan.taskmanager.controller;

import com.yusufmercan.taskmanager.dto.UserRequest;
import com.yusufmercan.taskmanager.dto.UserResponse;
import com.yusufmercan.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public UserResponse createUser(
            @Valid @RequestBody UserRequest request
    ) {
        return userService.createUser(request);
    }


    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll();
    }


    @GetMapping("/{id}")
    public UserResponse findById(
            @PathVariable Long id
    ) {
        return userService.findById(id);
    }


    @DeleteMapping("/{id}")
    public void deleteUser(
            @PathVariable Long id
    ) {
        userService.deleteUserById(id);
    }
}

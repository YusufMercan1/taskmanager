package com.yusufmercan.taskmanager.controller;

import com.yusufmercan.taskmanager.dto.LoginRequest;
import com.yusufmercan.taskmanager.entity.User;
import com.yusufmercan.taskmanager.repository.UserRepository;
import com.yusufmercan.taskmanager.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {


        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new RuntimeException("Wrong password");
        }


        return jwtService.generateToken(user.getEmail());
    }
}

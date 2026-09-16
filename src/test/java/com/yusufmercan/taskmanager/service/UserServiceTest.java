package com.yusufmercan.taskmanager.service;

import com.yusufmercan.taskmanager.dto.UserRequest;
import com.yusufmercan.taskmanager.dto.UserResponse;
import com.yusufmercan.taskmanager.entity.User;
import com.yusufmercan.taskmanager.exception.ResourceNotFoundException;
import com.yusufmercan.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {

        user = new User();

        user.setId(1L);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");
    }

    @Test
    void createUser_shouldCreateUserWithEncodedPassword() {

        UserRequest request = new UserRequest();

        request.setFirstName("Test");
        request.setLastName("User");
        request.setEmail("test@test.com");
        request.setPassword("password123");

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserResponse response = userService.createUser(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test", response.getFirstName());
        assertEquals("User", response.getLastName());
        assertEquals("test@test.com", response.getEmail());

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findAll_shouldReturnAllUsers() {

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        List<UserResponse> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Test", result.get(0).getFirstName());
        assertEquals("User", result.get(0).getLastName());
        assertEquals("test@test.com", result.get(0).getEmail());

        verify(userRepository).findAll();
    }

    @Test
    void findById_shouldReturnUserWhenUserExists() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserResponse response = userService.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test", response.getFirstName());
        assertEquals("User", response.getLastName());
        assertEquals("test@test.com", response.getEmail());
    }

    @Test
    void findById_shouldThrowExceptionWhenUserDoesNotExist() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.findById(999L)
        );
    }

    @Test
    void deleteUserById_shouldDeleteUserWhenUserExists() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.deleteUserById(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void deleteUserById_shouldThrowExceptionWhenUserDoesNotExist() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.deleteUserById(999L)
        );

        verify(userRepository, never()).delete(any(User.class));
    }
}
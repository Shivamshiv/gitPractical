package com.scaler.bookmyshow.services;

import com.scaler.bookmyshow.models.User;
import com.scaler.bookmyshow.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp_NewUser_Success() {
        // Arrange
        String email = "test@test.com";
        String name = "Test User";
        String password = "password";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("hashed_password");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName(name);
        savedUser.setEmail(email);
        savedUser.setPassword("hashed_password");
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.signUp(name, email, password);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(name, result.getName());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void signUp_ExistingUser_ThrowsException() {
        // Arrange
        String email = "test@test.com";
        String name = "Test User";
        String password = "password";

        User existingUser = new User();
        existingUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            userService.signUp(name, email, password)
        );

        assertEquals("User with email " + email + " already exists.", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }
}

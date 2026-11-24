package com.givfnz.moneymanager.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.givfnz.moneymanager.model.entities.UserModel;
import com.givfnz.moneymanager.model.repositories.UserRepository;
import com.givfnz.moneymanager.services.PasswordService;
import com.givfnz.moneymanager.services.TokenService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordService passwordService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserController userController;

    @Test
    void shouldCreateUser() {
        var user = new UserModel();
        user.setName("Giovani");
        user.setEmail("giovani@example.com");
        user.setPassword("plain-password");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(passwordService.hash("plain-password")).thenReturn("hashed-password");

        var response = userController.registerUser(user);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        assertEquals("hashed-password", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowConflictWhenEmailAlreadyExists() {
        var userRequest = new UserModel();
        userRequest.setEmail("giovani@example.com");
        userRequest.setPassword("plain-password");

        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(new UserModel());

        var exception = assertThrows(
                ResponseStatusException.class,
                () -> userController.registerUser(userRequest));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(userRepository, never()).save(any(UserModel.class));
        verifyNoInteractions(passwordService);
    }

    @Test
    void shouldLoginSuccessfully() {
        var userRequest = new UserModel();
        userRequest.setEmail("giovani@example.com");
        userRequest.setPassword("plain-password");

        var user = new UserModel();
        user.setEmail(userRequest.getEmail());
        user.setPassword("hashed-password");

        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(user);
        when(passwordService.matches(userRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(tokenService.generate(user)).thenReturn("jwt");

        var response = userController.loginUser(userRequest);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    }

    @Test
    void shouldNotLoginWithWrongCredentials() {
        var userRequest = new UserModel();
        userRequest.setEmail("giovani@example.com");
        userRequest.setPassword("plain-password");

        var user = new UserModel();
        user.setEmail(userRequest.getEmail());
        user.setPassword("wrong-password");

        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(user);
        when(passwordService.matches(userRequest.getPassword(), user.getPassword())).thenReturn(false);

        var exception = assertThrows(
                ResponseStatusException.class,
                () -> userController.loginUser(userRequest));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Password does not match.", exception.getReason());
    }
}

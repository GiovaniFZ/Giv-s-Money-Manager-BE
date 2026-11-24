package com.givfnz.moneymanager.controllers;

import com.givfnz.moneymanager.model.entities.UserModel;
import com.givfnz.moneymanager.model.repositories.UserRepository;
import com.givfnz.moneymanager.services.PasswordService;
import com.givfnz.moneymanager.services.TokenService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    private UserRepository userRepository;
    private PasswordService passwordService;
    private TokenService tokenService;

    public UserController(UserRepository userRepository, PasswordService passwordService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserModel> registerUser(@RequestBody UserModel userRequest) {
        verifyUserForRegister(userRequest);
        hashUserPassword(userRequest);
        createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private void verifyUserForRegister(UserModel userRequest) {
        var user = this.userRepository.findByEmail(userRequest.getEmail());
        if (user != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
    }

    private void hashUserPassword(UserModel userRequest) {
        var passwordHashed = passwordService.hash(userRequest.getPassword());
        userRequest.setPassword(passwordHashed);
    }

    private void createUser(UserModel userRequest) {
        this.userRepository.save(userRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserModel userRequest) {
        var user = verifyUserForSignIn(userRequest);
        verifyIfPasswordMatches(userRequest, user);
        String jwt = generateJWT(user);
        return ResponseEntity.ok(Map.of("access_token", jwt));
    }

    private UserModel verifyUserForSignIn(UserModel userRequest) {
        var user = this.userRepository.findByEmail(userRequest.getEmail());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User was not found");
        }
        return user;
    }

    private void verifyIfPasswordMatches(UserModel userRequest, UserModel user) {
        String inputPassword = userRequest.getPassword();
        String realPasswordHash = user.getPassword();
        boolean doesPasswordMatches = this.passwordService.matches(inputPassword, realPasswordHash);
        if (!doesPasswordMatches) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password does not match.");
        }
    }

    private String generateJWT(UserModel user) {
        return this.tokenService.generate(user);
    }

}

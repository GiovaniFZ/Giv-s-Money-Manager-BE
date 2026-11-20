package com.givfnz.moneymanager.controllers;
import com.givfnz.moneymanager.model.entities.UserModel;
import com.givfnz.moneymanager.model.repositories.UserRepository;
import com.givfnz.moneymanager.services.PasswordService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(value = "/api/user")
public class CreateUserController {
    private UserRepository userRepository;
    private PasswordService passwordService;

    public CreateUserController(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserModel> registerUser(@RequestBody UserModel userRequest) {    
        verifyIfUserAlreadyExists(userRequest);
        hashUserPassword(userRequest);
        createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private void verifyIfUserAlreadyExists(UserModel userRequest) {
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
    
}

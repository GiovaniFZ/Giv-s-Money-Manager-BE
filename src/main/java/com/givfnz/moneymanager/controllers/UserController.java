package com.givfnz.moneymanager.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    @PostMapping("/register")
    public ResponseEntity registerUser() {
        
        return ResponseEntity.noContent().build();
    }
    
}

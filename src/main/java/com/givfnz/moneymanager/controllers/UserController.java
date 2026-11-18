package com.givfnz.moneymanager.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    @PostMapping("/register")
    public String registerUser(@RequestBody String entity) {
        return "Success!";
    }
    
}

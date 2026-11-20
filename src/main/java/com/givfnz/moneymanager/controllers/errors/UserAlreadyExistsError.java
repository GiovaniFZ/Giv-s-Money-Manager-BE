package com.givfnz.moneymanager.controllers.errors;

public class UserAlreadyExistsError extends Exception {
    public UserAlreadyExistsError() {
        super("User already exists");
    }
}

package com.givfnz.moneymanager.services;

public interface PasswordService {
    public String hash(String password);
    public boolean matches(String password, String hash);
}

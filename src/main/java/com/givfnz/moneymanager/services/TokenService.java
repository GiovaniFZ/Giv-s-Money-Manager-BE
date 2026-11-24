package com.givfnz.moneymanager.services;

import com.givfnz.moneymanager.model.entities.UserModel;

public interface TokenService {
    public String generate(UserModel userModel);
}

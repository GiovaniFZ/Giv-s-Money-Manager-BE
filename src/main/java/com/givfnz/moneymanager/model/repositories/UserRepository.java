package com.givfnz.moneymanager.model.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.givfnz.moneymanager.model.entities.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID>  {
    UserModel findByEmail(String email);
}

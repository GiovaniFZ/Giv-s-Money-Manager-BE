package com.givfnz.moneymanager.model.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_income")
public class IncomeModel {   

    public enum IncomeType {
        FIXED,
        VARIABLE,
        EXTRA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private UUID userId;
    private String description;
    private int amount;
    private UUID categoryId;
    private IncomeType income;

    @CreationTimestamp
    private LocalDateTime date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

package com.effective_mobile.test_project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = true)
    private BigDecimal initialBalance;

    public BankAccount(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
        this.balance = initialBalance;
    }

    // метод для увеличения баланса
    public void increaseBalance(BigDecimal percentage) {
        BigDecimal increaseAmount = balance.multiply(percentage);
        balance = balance.add(increaseAmount).min(BigDecimal.valueOf(207)); // не более 207% от начального депозита
    }

    // метод для уменьшения баланса (по необходимости)
    public boolean decreaseBalance(BigDecimal amount) {
        if (balance.compareTo(amount) >= 0) {
            balance = balance.subtract(amount);
            return true;
        }
        return false;
    }
}

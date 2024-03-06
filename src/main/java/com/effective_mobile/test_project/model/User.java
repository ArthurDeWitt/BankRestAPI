package com.effective_mobile.test_project.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phone;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private BankAccount account;

    // Методы для изменения контактных данных с проверкой согласно условиям
    public void changeEmail(String newEmail) {
        if (isValidContactChange(newEmail, this.email)) {
            this.email = newEmail;
        }
    }

    public void changePhone(String newPhone) {
        if (isValidContactChange(newPhone, this.phone)) {
            this.phone = newPhone;
        }
    }

    public void deleteEmail() {
        this.email = null;
    }

    public void deletePhone() {
        this.phone = null;
    }

    public boolean canDeleteEmail() {
        return hasBothContacts();
    }

    public boolean canDeletePhone() {
        return hasBothContacts();
    }

    private boolean isValidContactChange(String newContact, String currentContact) {
        return newContact != null && !newContact.equals(currentContact);
    }

    private boolean hasBothContacts() {
        return this.email != null && this.phone != null;
    }
}
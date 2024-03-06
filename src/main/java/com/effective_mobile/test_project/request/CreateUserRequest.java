package com.effective_mobile.test_project.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreateUserRequest {
    private String username;
    private String password;
    private String name;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
    private BigDecimal initialBalance;
}

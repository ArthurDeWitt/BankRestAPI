package com.effective_mobile.test_project.services;

import com.effective_mobile.test_project.model.User;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(String username, String password, String name, LocalDate dateOfBirth, String email, String phone, BigDecimal initialBalance);

    User updateUserContacts(String username, String newEmail, String newPhone);

    User deleteUserContacts(String username, String contactType);

    List<User> searchUsers(LocalDate dateOfBirth, String phone, String name, String email, Pageable pageable);

    void increaseUserBalances();

    void transferMoney(Long senderUserId, Long receiverUserId, BigDecimal amount);

    User findById(Long userId);

    Optional<User> findByUsername(String username);

    void updateUser(User user);
}
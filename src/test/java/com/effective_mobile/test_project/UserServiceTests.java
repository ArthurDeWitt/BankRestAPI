package com.effective_mobile.test_project;

import com.effective_mobile.test_project.custom_excepions.InsufficientFundsException;
import com.effective_mobile.test_project.model.User;
import com.effective_mobile.test_project.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static jdk.internal.org.jline.utils.InfoCmp.Capability.user1;
import static jdk.internal.org.jline.utils.InfoCmp.Capability.user2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceTests {

    private UserService userService;
    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        userService = Mockito.mock(UserService.class);

        // Initialize users here
        user1 = new User();
        user1.setId(1L);

        user2 = new User();
        user2.setId(2L);
    }

    @Test
    public void testSearchUsers() {
        // Given
        LocalDate birthDate = LocalDate.of(1998, 12, 15);
        String phone = "+1234567890";
        String fullName = "Test User";
        String email = "test@mail.ru";

        // When
        Page<User> mockPage = Mockito.mock(Page.class);
        Mockito.when(userService.
                searchUsers(user1.getId(), birthDate, phone, fullName, email, PageRequest.of(0, 10))).thenReturn(mockPage);

        // Then
        Assertions.assertEquals(1, usersPage.getTotalElements(), "One user should be found");
        Assertions.assertEquals(user1.getId(), usersPage.getContent().get(0).getId(), "User ID should match");
    }

    @Test
    public void testTransferMoney() {
        // Given
        BigDecimal amount = BigDecimal.valueOf(500);

        // When
        userService.transferMoney(user1.getId(), user2.getId(), amount);

        // Then
        User userFromDb1 = userService.findById(user1.getId()).orElseThrow();
        User userFromDb2 = userService.findById(user2.getId()).orElseThrow();

        assertEquals(BigDecimal.valueOf(500), userFromDb1.getAccount().getBalance(), "User1 balance should be updated");
        assertEquals(BigDecimal.valueOf(1500), userFromDb2.getAccount().getBalance(), "User2 balance should be updated");
    }

    @Test
    public void testTransferMoney_insufficientFunds() {
        // Given
        BigDecimal amount = BigDecimal.valueOf(2000);

        // When & Then
        assertThrows(
                InsufficientFundsException.class,
                () -> userService.transferMoney(user1.getId(), user2.getId(), amount),
                "Should throw InsufficientFundsException when transferring more funds than available"
        );
    }
}

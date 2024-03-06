package com.effective_mobile.test_project.services;

import com.effective_mobile.test_project.custom_excepions.InsufficientFundsException;
import com.effective_mobile.test_project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@ComponentScan
public class TransferServiceImpl implements TransferService {

    private final UserService userService;

    @Autowired
    public TransferServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public void transferMoney(String senderUsername, String receiverUsername, BigDecimal amount) {
        Optional<User> senderOptional = userService.findByUsername(senderUsername);
        Optional<User> receiverOptional = userService.findByUsername(receiverUsername);

        if (senderOptional.isEmpty() || receiverOptional.isEmpty()) {
            throw new IllegalArgumentException("Sender or receiver not found");
        }

        User sender = senderOptional.get();
        User receiver = receiverOptional.get();

        if (!sender.getAccount().decreaseBalance(amount)) {
            throw new InsufficientFundsException("Insufficient funds in the sender's account");
        }

        receiver.getAccount().increaseBalance(amount);

        // Сохраняем изменения в базе данных
        userService.updateUser(sender);
        userService.updateUser(receiver);
    }
}
package com.effective_mobile.test_project.services;

import java.math.BigDecimal;

public interface TransferService {
    void transferMoney(String senderUsername, String receiverUsername, BigDecimal amount);
}

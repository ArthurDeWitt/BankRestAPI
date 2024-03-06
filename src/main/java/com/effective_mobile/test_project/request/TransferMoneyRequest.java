package com.effective_mobile.test_project.request;

import lombok.*;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TransferMoneyRequest {
    private String senderUsername;
    private String receiverUsername;
    private BigDecimal amount;
}

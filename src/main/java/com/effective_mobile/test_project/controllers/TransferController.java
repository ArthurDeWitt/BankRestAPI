package com.effective_mobile.test_project.controllers;

import com.effective_mobile.test_project.request.TransferMoneyRequest;
import com.effective_mobile.test_project.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping("/money")
    public ResponseEntity<String> transferMoney(@RequestBody TransferMoneyRequest request) {
        try {
            transferService.transferMoney(request.getSenderUsername(), request.getReceiverUsername(), request.getAmount());
            return new ResponseEntity<>("Перевод выполнен успешно", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

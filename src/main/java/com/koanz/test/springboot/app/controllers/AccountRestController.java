package com.koanz.test.springboot.app.controllers;

import com.koanz.test.springboot.app.models.dtos.TransactionDto;
import com.koanz.test.springboot.app.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {

    @Autowired
    private AccountService service;

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetailById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> wireMoney(@RequestBody TransactionDto transaction) {
        service.wireMoneyFromTo(transaction.getAccountOriginId(), transaction.getAccountDestinyId(), transaction.getAmount(), transaction.getBankId());

        Map<String, Object> message = new HashMap<>();
        message.put("date", LocalDate.now().toString());
        message.put("status", "OK");
        message.put("message", "The Transfer has been completed.");
        message.put("transaction", transaction);

        return ResponseEntity.ok(message);
    }
}

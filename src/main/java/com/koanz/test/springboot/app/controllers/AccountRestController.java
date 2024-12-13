package com.koanz.test.springboot.app.controllers;

import com.koanz.test.springboot.app.models.Account;
import com.koanz.test.springboot.app.models.dtos.TransactionDto;
import com.koanz.test.springboot.app.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {

    @Autowired
    private AccountService service;

    @GetMapping("/detail/{id}")
    public ResponseEntity<Account> getDetailById(@PathVariable Long id) {
        Account account = null;

        try {
            account = service.findById(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(account);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.findAll());
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

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Account request) {
        Account newAccount = service.save(request);

        Map<String, Object> message = new HashMap<>();
        message.put("date", LocalDate.now().toString());
        message.put("status", "OK");
        message.put("message", "The Account has been created.");
        message.put("account", newAccount);

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);

        Map<String, Object> message = new HashMap<>();
        message.put("date", LocalDate.now().toString());
        message.put("status", "OK");
        message.put("message", "The Account has been deleted.");

        return ResponseEntity.ok(message);
    }
}

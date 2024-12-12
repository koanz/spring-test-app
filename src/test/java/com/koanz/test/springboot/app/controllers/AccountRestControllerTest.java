package com.koanz.test.springboot.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koanz.test.springboot.app.models.Account;
import com.koanz.test.springboot.app.models.dtos.AccountDto;
import com.koanz.test.springboot.app.models.dtos.TransactionDto;
import com.koanz.test.springboot.app.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static com.koanz.test.springboot.app.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountRestController.class)
class AccountRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AccountService service;

    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void testGetDetailById() throws Exception {
        Long accountId = 1L;
        // Given
        when(service.findById(accountId)).thenReturn(createAccount001().orElseThrow());

        // When
        mvc.perform(get("/api/accounts/detail/{id}", accountId).contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person").value("John Doe"))
                .andExpect(jsonPath("$.balance").value("1000"));

        verify(service).findById(accountId);
    }

    @Test
    void testWireMoney() throws Exception, JsonProcessingException {
        Long accountOriginId = 1L, accountDestinyId = 2L;
        BigDecimal amount = new BigDecimal("100");
        // Given
        TransactionDto requestTransaction = new TransactionDto();
        requestTransaction.setAccountOriginId(accountOriginId);
        requestTransaction.setAccountDestinyId(accountDestinyId);
        requestTransaction.setBankId(1L);
        requestTransaction.setAmount(amount);

        Map<String, Object> message = new HashMap<>();
        message.put("date", LocalDate.now().toString());
        message.put("status", "OK");
        message.put("message", "The Transfer has been completed.");
        message.put("transaction", requestTransaction);

        // When
        mvc.perform(post("/api/accounts/transfer").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestTransaction)))

        // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value(message.get("date")))
                .andExpect(jsonPath("$.message").value(message.get("message")))
                .andExpect(jsonPath("$.transaction.account_origin_id").value(requestTransaction.getAccountOriginId()))
                .andExpect(content().json(mapper.writeValueAsString(message)));
    }

    @Test
    void testGetAccounts() throws Exception {
        // Given
        List<Account> accounts = Arrays.asList(createAccount001().orElseThrow(),
                createAccount002().orElseThrow());

        // When
        when(service.findAll()).thenReturn(accounts);

        mvc.perform(get("/api/accounts/list").contentType(MediaType.APPLICATION_JSON))
        // Then
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$[0].person").value("John Doe"))
                        .andExpect(jsonPath("$[0].balance").value("1000"))
                        .andExpect(jsonPath("$[1].person").value("Jane Doe"))
                        .andExpect(jsonPath("$[1].balance").value("2000"))
                        .andExpect(jsonPath("$", hasSize(2)))
                        .andExpect(content().json(mapper.writeValueAsString(accounts)));

        verify(service).findAll();
    }

    @Test
    void testSave() throws Exception {
        // Given
        Account newAccount = new Account(null, "Pepe", new BigDecimal("3000"));
        when(service.save(any())).then(invocationOnMock -> {
            Account a = invocationOnMock.getArgument(0);
            a.setId(3L);
            return a;
        });

        // When
        mvc.perform(post("/api/accounts/create").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newAccount)))
        // Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.account.id", is(3)))
                .andExpect(jsonPath("$.account.person", is("Pepe")))
                .andExpect(jsonPath("$.account.balance", is(3000)));

        verify(service).save(any());
    }
}
package com.koanz.test.springboot.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koanz.test.springboot.app.models.dtos.TransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = RANDOM_PORT)
class AccountRestControllerWebTestClientTest {

    private ObjectMapper mapper;

    @Autowired
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void testWireMoney() throws JsonProcessingException {
        // Given
        TransactionDto request = new TransactionDto();
        request.setAccountOriginId(1L);
        request.setAccountDestinyId(2L);
        request.setBankId(1L);
        request.setAmount(new BigDecimal("100"));

        Map<String, Object> message = new HashMap<>();
        message.put("date", LocalDate.now().toString());
        message.put("status", "OK");
        message.put("message", "The Transfer has been completed.");
        message.put("transaction", request);

        // When
        client.post().uri("/api/accounts/transfer")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
        // Then
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    try {
                        JsonNode json = mapper.readTree(response.getResponseBody());
                        assertEquals(LocalDate.now().toString(), json.path("date").asText());
                        assertEquals("The Transfer has been completed.", json.path("message").asText());
                        assertEquals(1L, json.path("transaction").path("account_origin_id").asLong());
                        assertEquals(2L, json.path("transaction").path("account_destiny_id").asLong());
                        assertEquals(1L, json.path("transaction").path("bank_id").asLong());
                        assertEquals("100", json.path("transaction").path("amount").asText());
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                })
                /*.jsonPath("$.message").isNotEmpty()
                .jsonPath("$.message").value(is("The Transfer has been completed."))
                .jsonPath("$.message").value(value -> assertEquals("The Transfer has been completed.", value))
                .jsonPath("$.message").isEqualTo("The Transfer has been completed.")
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                .jsonPath("$.transaction.account_origin_id").isEqualTo(request.getAccountOriginId())
                .jsonPath("$.transaction.account_destiny_id").isEqualTo(request.getAccountDestinyId())
                .jsonPath("$.transaction.bank_id").isEqualTo(request.getBankId())
                .jsonPath("$.transaction.amount").isEqualTo(request.getAmount())*/
                .json(mapper.writeValueAsString(message));

    }
}
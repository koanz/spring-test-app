package com.koanz.test.springboot.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koanz.test.springboot.app.models.Account;
import com.koanz.test.springboot.app.models.dtos.TransactionDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.class)
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
    @Order(1)
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

    @Test
    @Order(2)
    void testgetDetailById1() {
        // Given
        Account expected = new Account(1L, "John Doe", new BigDecimal("900.00"));

        // When
        client.get().uri("/api/accounts/detail/{id}", expected.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
        // Then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class)
                .consumeWith(response -> {
                    Account received = response.getResponseBody();
                    assertEquals(expected.getId(), received.getId());
                    assertEquals(expected.getPerson(), received.getPerson());
                    assertEquals(expected.getBalance(), received.getBalance());
                });
    }

    @Test
    @Order(3)
    void testgetDetailById2() {
        // Given
        Account expected = new Account(2L, "Jane Doe", new BigDecimal("2100.00"));

        // When
        client.get().uri("/api/accounts/detail/{id}", expected.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // Then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class)
                .consumeWith(response -> {
                    Account account = response.getResponseBody();
                    assertEquals(expected.getId(), account.getId());
                    assertEquals(expected.getPerson(), account.getPerson());
                    assertEquals(expected.getBalance(), account.getBalance());
                });
    }

    @Test
    void testGetAccount() {
        // Given
        List<Account> expected = Arrays.asList(
                new Account(1L, "John Doe", new BigDecimal("900.00")),
                new Account(2L, "Jane Doe", new BigDecimal("2100.00")));

        // When
        client.get().uri("/api/accounts/list")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
        // Then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(List.class)
                .consumeWith(response -> {
                    List<Account> received = (List<Account>) response.getResponseBody();

                });
    }
}
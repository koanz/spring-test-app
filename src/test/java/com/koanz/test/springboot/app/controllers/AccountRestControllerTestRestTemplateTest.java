package com.koanz.test.springboot.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koanz.test.springboot.app.models.dtos.TransactionDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountRestControllerTestRestTemplateTest {

    @Autowired
    private TestRestTemplate client;

    @LocalServerPort
    private int port;

    private ObjectMapper mapper;

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

        // When
        ResponseEntity<String> responseEntity = client.
                postForEntity(this.getUri("/api/accounts/transfer"), request, String.class);

        // Then
        String response = responseEntity.getBody();
        //System.out.println(response);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertNotNull(response);
        assertTrue(response.contains("The Transfer has been completed."));
        assertTrue(response.contains("{\"date\":\"2024-12-13\",\"message\":\"The Transfer has been completed.\"," +
                "\"transaction\":{\"amount\":100,\"account_origin_id\":1,\"account_destiny_id\":2,\"bank_id\":1}," +
                "\"status\":\"OK\"}"));

        JsonNode json = mapper.readTree(response);
        assertEquals(LocalDate.now().toString(), json.path("date").asText());
        assertEquals("The Transfer has been completed.", json.path("message").asText());
        assertEquals(1L, json.path("transaction").path("account_origin_id").asLong());
        assertEquals(2L, json.path("transaction").path("account_destiny_id").asLong());
        assertEquals(1L, json.path("transaction").path("bank_id").asLong());
        assertEquals("100", json.path("transaction").path("amount").asText());

        Map<String, Object> message = new HashMap<>();
        message.put("date", LocalDate.now().toString());
        message.put("status", "OK");
        message.put("message", "The Transfer has been completed.");
        message.put("transaction", request);

        assertEquals(mapper.writeValueAsString(message), response);
    }

    private String getUri(String endpoint) {
        return "http://localhost:" + port + endpoint;
    }
}
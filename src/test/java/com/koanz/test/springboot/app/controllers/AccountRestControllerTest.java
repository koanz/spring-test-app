package com.koanz.test.springboot.app.controllers;

import com.koanz.test.springboot.app.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
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

}
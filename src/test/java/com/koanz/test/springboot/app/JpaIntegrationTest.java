package com.koanz.test.springboot.app;

import com.koanz.test.springboot.app.models.Account;
import com.koanz.test.springboot.app.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class JpaIntegrationTest {
    @Autowired
    AccountRepository accountRepository;

    @Test
    void testFindById() {
        Optional<Account> account = accountRepository.findById(1L);

        assertTrue(account.isPresent());
        assertEquals(1L, account.orElseThrow().getId());
        assertEquals("John Doe", account.orElseThrow().getPerson());
        assertEquals(new BigDecimal("1000.00"), account.orElseThrow().getBalance());
    }

    @Test
    void testFindByPersona() {
        Optional<Account> account = accountRepository.findByPerson("John Doe");

        assertTrue(account.isPresent());
        assertEquals(1L, account.orElseThrow().getId());
        assertEquals("John Doe", account.orElseThrow().getPerson());
        assertEquals(new BigDecimal("1000.00"), account.orElseThrow().getBalance());
    }

    @Test
    void testFindByPersonaThrowException() {
        Optional<Account> account = accountRepository.findByPerson("John");

        assertThrows(NoSuchElementException.class, account::orElseThrow);

        assertFalse(account.isPresent());
    }

    @Test
    void testFindAll() {
        List<Account> accounts = accountRepository.findAll();
        assertFalse(accounts.isEmpty());
        assertEquals(2, accounts.size());
    }

    @Test
    void testSaveAccount() {
        // Given
        Account accountPepe = new Account(null, "Pepe", new BigDecimal("3000"));

        // When
        Account account = accountRepository.save(accountPepe);
        //Account account = accountRepository.findByPerson("Pepe").orElseThrow();
        //Account account = accountRepository.findById(save.getId()).orElseThrow();

        // Then
        assertEquals("Pepe", account.getPerson());
        assertEquals("3000", account.getBalance().toPlainString());
//        assertEquals(3, account.getId());
    }

    @Test
    void testUpdateAccount() {
        // Given
        Account accountPepe = new Account(null, "Pepe", new BigDecimal("3000"));

        // When
        Account account = accountRepository.save(accountPepe);
        //Account account = accountRepository.findByPerson("Pepe").orElseThrow();
        //Account account = accountRepository.findById(save.getId()).orElseThrow();

        // Then
        assertEquals("Pepe", account.getPerson());
        assertEquals("3000", account.getBalance().toPlainString());

        // When
        account.setBalance(new BigDecimal("3800"));
        Account accountUpdated = accountRepository.save(account);

        // Then
        assertEquals("Pepe", accountUpdated.getPerson());
        assertEquals("3800", accountUpdated.getBalance().toPlainString());
    }

    @Test
    void testDeleteAccount() {
        Account account = accountRepository.findById(2L).orElseThrow();

        assertEquals("Jane Doe", account.getPerson());

        accountRepository.delete(account);
        assertThrows(NoSuchElementException.class, () -> {
//            accountRepository.findByPerson("Jane Doe").orElseThrow();
            accountRepository.findById(2L).orElseThrow();
        });

        assertEquals(1, accountRepository.findAll().size());
    }
}

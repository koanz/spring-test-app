package com.koanz.test.springboot.app;

import static org.mockito.Mockito.*;

import com.koanz.test.springboot.app.repositories.AccountRepository;
import com.koanz.test.springboot.app.repositories.BankRepository;
import com.koanz.test.springboot.app.services.AccountService;
import com.koanz.test.springboot.app.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootTestApplicationTests {

	AccountRepository accountRepository;

	BankRepository bankRepository;

	AccountService service;

	@BeforeEach
	void setUp() {
		accountRepository = mock(AccountRepository.class);
		bankRepository = mock(BankRepository.class);
		service = new AccountServiceImpl(accountRepository, bankRepository);
	}

	@Test
	void contextLoads() {
	}

}

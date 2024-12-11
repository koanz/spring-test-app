package com.koanz.test.springboot.app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.koanz.test.springboot.app.Data.*;

import com.koanz.test.springboot.app.exceptions.NotEnoughMoneyException;
import com.koanz.test.springboot.app.models.Account;
import com.koanz.test.springboot.app.models.Bank;
import com.koanz.test.springboot.app.repositories.AccountRepository;
import com.koanz.test.springboot.app.repositories.BankRepository;
import com.koanz.test.springboot.app.services.AccountService;
import com.koanz.test.springboot.app.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;

@SpringBootTest
class SpringbootTestApplicationTests {

	@MockitoBean
	AccountRepository accountRepository;

	@MockitoBean
	BankRepository bankRepository;

	@Autowired
	AccountService service;

	@BeforeEach
	void setUp() {
		//accountRepository = mock(AccountRepository.class);
		//bankRepository = mock(BankRepository.class);
		//service = new AccountServiceImpl(accountRepository, bankRepository);
	}

	@Test
	void contextLoads() {
		when(accountRepository.findById(1L)).thenReturn(createAccount001());
		when(accountRepository.findById(2L)).thenReturn(createAccount002());
		when(bankRepository.findById(1L)).thenReturn(createBank());

		BigDecimal balanceAccountOrigin = service.checkBalance(1L);
		BigDecimal balanceAccountDestiny = service.checkBalance(2L);

		assertEquals("10000", balanceAccountOrigin.toPlainString());
		assertEquals("3000", balanceAccountDestiny.toPlainString());

		service.wireMoneyFromTo(1L, 2L, new BigDecimal("2500"), 1L);

		balanceAccountOrigin = service.checkBalance(1L);
		balanceAccountDestiny = service.checkBalance(2L);
		int totalTransferBank = service.getTotalTransfer(1L);

		assertEquals("7500", balanceAccountOrigin.toPlainString());
		assertEquals("5500", balanceAccountDestiny.toPlainString());
		assertEquals(1, totalTransferBank);

		verify(accountRepository, times(3)).findById(1L);
		verify(accountRepository, times(3)).findById(2L);
		verify(accountRepository, times(2)).update(any(Account.class));

		verify(bankRepository, times(2)).findById(1L);
		verify(bankRepository).update(any(Bank.class));

		verify(accountRepository, times(6)).findById(anyLong());
		verify(accountRepository, never()).findAll();
	}

	@Test
	void contextLoads2() {
		when(accountRepository.findById(1L)).thenReturn(createAccount001());
		when(accountRepository.findById(2L)).thenReturn(createAccount002());
		when(bankRepository.findById(1L)).thenReturn(createBank());

		BigDecimal balanceAccountOrigin = service.checkBalance(1L);
		BigDecimal balanceAccountDestiny = service.checkBalance(2L);

		assertEquals("10000", balanceAccountOrigin.toPlainString());
		assertEquals("3000", balanceAccountDestiny.toPlainString());

		assertThrows(NotEnoughMoneyException.class, () -> {
			service.wireMoneyFromTo(1L, 2L, new BigDecimal("11000"), 1L);
		});

		balanceAccountOrigin = service.checkBalance(1L);
		balanceAccountDestiny = service.checkBalance(2L);

		assertEquals("10000", balanceAccountOrigin.toPlainString());
		assertEquals("3000", balanceAccountDestiny.toPlainString());

		int totalTransferBank = service.getTotalTransfer(1L);
		assertEquals(0, totalTransferBank);

		verify(accountRepository, times(3)).findById(1L);
		verify(accountRepository, times(2)).findById(2L);
		verify(accountRepository, never()).update(any(Account.class));

		verify(bankRepository, times(1)).findById(1L);
		verify(bankRepository, never()).update(any(Bank.class));

		verify(accountRepository, times(5)).findById(anyLong());
		verify(accountRepository, never()).findAll();
	}

	@Test
	void contextLoads3() {
		when(accountRepository.findById(1L)).thenReturn(createAccount001());

		Account account001 = service.findById(1L);
		Account account002 = service.findById(1L);

		assertSame(account001, account002);
		assertTrue(account001 == account002);
		assertEquals("John Doe", account001.getPerson());
		assertEquals("John Doe", account002.getPerson());
		verify(accountRepository, times(2)).findById(1L);
	}
}

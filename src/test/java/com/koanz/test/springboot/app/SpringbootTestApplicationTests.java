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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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

		assertEquals("1000", balanceAccountOrigin.toPlainString());
		assertEquals("2000", balanceAccountDestiny.toPlainString());

		service.wireMoneyFromTo(1L, 2L, new BigDecimal("100"), 1L);

		balanceAccountOrigin = service.checkBalance(1L);
		balanceAccountDestiny = service.checkBalance(2L);
		int totalTransferBank = service.getTotalTransfer(1L);

		assertEquals("900", balanceAccountOrigin.toPlainString());
		assertEquals("2100", balanceAccountDestiny.toPlainString());
		assertEquals(1, totalTransferBank);

		verify(accountRepository, times(3)).findById(1L);
		verify(accountRepository, times(3)).findById(2L);
		verify(accountRepository, times(2)).save(any(Account.class));

		verify(bankRepository, times(2)).findById(1L);
		verify(bankRepository).save(any(Bank.class));

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

		assertEquals("1000", balanceAccountOrigin.toPlainString());
		assertEquals("2000", balanceAccountDestiny.toPlainString());

		assertThrows(NotEnoughMoneyException.class, () -> {
			service.wireMoneyFromTo(1L, 2L, new BigDecimal("1100"), 1L);
		});

		balanceAccountOrigin = service.checkBalance(1L);
		balanceAccountDestiny = service.checkBalance(2L);

		assertEquals("1000", balanceAccountOrigin.toPlainString());
		assertEquals("2000", balanceAccountDestiny.toPlainString());

		int totalTransferBank = service.getTotalTransfer(1L);
		assertEquals(0, totalTransferBank);

		verify(accountRepository, times(3)).findById(1L);
		verify(accountRepository, times(2)).findById(2L);
		verify(accountRepository, never()).save(any(Account.class));

		verify(bankRepository, times(1)).findById(1L);
		verify(bankRepository, never()).save(any(Bank.class));

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

	@Test
	void testFindAllAccounts() {
		// Given
		List<Account> accounts = Arrays.asList(createAccount001().orElseThrow(),
				createAccount002().orElseThrow());
		when(accountRepository.findAll()).thenReturn(accounts);

		// When
		List<Account> accounts1 = service.findAll();

		// Then
		assertFalse(accounts1.isEmpty());
		assertEquals(2, accounts1.size());
		assertTrue(accounts1.contains(createAccount001().orElseThrow()));

		verify(accountRepository).findAll();
	}

	@Test
	void testSaveAccount() {
		// Given
		Account newAccount = new Account(null, "Pepe", new BigDecimal("3000"));
		when(accountRepository.save(any())).then(invocationOnMock -> {
			Account c = invocationOnMock.getArgument(0);
			c.setId(3L);
			return c;
		});

		// When
		Account account = service.save(newAccount);

		// Then
		assertEquals(3, account.getId());
		assertEquals("Pepe", account.getPerson());
		assertEquals("3000", account.getBalance().toPlainString());

		verify(accountRepository).save(any());
	}
}

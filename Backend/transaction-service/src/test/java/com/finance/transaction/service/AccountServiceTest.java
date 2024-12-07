package com.finance.transaction.service;

import com.finance.transaction.model.Account;
import com.finance.transaction.dto.CustomResponse;
import com.finance.transaction.exceptions.DuplicateAccountNumberException;
import com.finance.transaction.exceptions.ResourceNotFoundException;
import com.finance.transaction.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionService transactionService;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        account = new Account();
        account.setAccountNumber("123456789");
        account.setUserId(1L);
        account.setAccountBalance(1000.0);
    }

    @Test
    void testGetAccountsByUserId_Success() {
        // Mocking the repository method
        when(accountRepository.findAllByUserId(1L)).thenReturn(List.of(account));

        CustomResponse<List<Account>> response = accountService.getAccountsByUserId(1L);

        assertNotNull(response);
        assertEquals("Accounts retrieved successfully", response.getMessage());
        assertEquals(1, response.getData().size());
        assertEquals(account, response.getData().get(0));
    }

    @Test
    void testGetAccountsByUserId_NoAccounts() {
        // Mocking the repository method to return an empty list
        when(accountRepository.findAllByUserId(1L)).thenReturn(List.of());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccountsByUserId(1L);
        });

        assertEquals("No accounts found for userId: 1", exception.getMessage());
    }

    @Test
    void testAddAccount_Success() {
        // Mocking the repository method
        when(accountRepository.existsByAccountNumber("123456789")).thenReturn(false);
        when(accountRepository.save(account)).thenReturn(account);

        CustomResponse<Account> response = accountService.addAccount(account);

        assertNotNull(response);
        assertEquals("Account created successfully", response.getMessage());
        assertEquals(account, response.getData());
    }

    @Test
    void testAddAccount_DuplicateAccountNumber() {
        // Mocking the repository method to simulate existing account
        when(accountRepository.existsByAccountNumber("123456789")).thenReturn(true);

        Exception exception = assertThrows(DuplicateAccountNumberException.class, () -> {
            accountService.addAccount(account);
        });

        assertEquals("An account with the account number 123456789 already exists.", exception.getMessage());
    }

    @Test
    void testDeleteAccount_Success() {
        // Mocking the repository methods
        when(accountRepository.existsById(1L)).thenReturn(true);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        CustomResponse<Account> response = accountService.deleteAccount(1L);

        assertNotNull(response);
        assertEquals("Account deleted successfully", response.getMessage());
        assertEquals(account, response.getData());

        verify(accountRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAccount_AccountNotFound() {
        // Mocking the repository method to simulate account not found
        when(accountRepository.existsById(1L)).thenReturn(false);

        CustomResponse<Account> response = accountService.deleteAccount(1L);

        assertNotNull(response);
        assertEquals("Account not found for accountId: 1", response.getMessage());
        assertNull(response.getData());
    }
}

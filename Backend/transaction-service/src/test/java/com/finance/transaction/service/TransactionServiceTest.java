package com.finance.transaction.service;

import com.finance.transaction.model.Account;
import com.finance.transaction.model.Transaction;
import com.finance.transaction.model.TransactionType;
import com.finance.transaction.client.BudgetServiceClient;
import com.finance.transaction.dto.CustomResponse;
import com.finance.transaction.exceptions.InsufficientFundsException;
import com.finance.transaction.repository.AccountRepository;
import com.finance.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BudgetServiceClient budgetServiceClient;

    private Account account;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        account = new Account();
        account.setAccountNumber("123456789");
        account.setUserId(1L);
        account.setAccountBalance(1000.0);

        transaction = new Transaction();
        transaction.setAccountNumber("123456789");
        transaction.setAmount(100.0);
        transaction.setUserId(1L);
        transaction.setDescription("Test transaction");
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setCategoryType(null);
    }

    @Test
    void testGetTransactions_Success() {
        when(transactionRepository.findAllByUserId(1L)).thenReturn(List.of(transaction));

        List<Transaction> transactions = transactionService.getTransactions(1L);

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));
    }

    @Test
    void testAddTransaction_Success_Deposit() {
        when(accountRepository.findByAccountNumber("123456789")).thenReturn(account);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        CustomResponse<Transaction> response = transactionService.addTransaction(transaction);

        assertNotNull(response);
        assertEquals("Transaction successful", response.getMessage());
        assertEquals(transaction, response.getData());
        assertEquals(900.0, account.getAccountBalance());
    }

    @Test
    void testAddTransaction_Success_Withdraw() {
        when(accountRepository.findByAccountNumber("123456789")).thenReturn(account);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        CustomResponse<Transaction> response = transactionService.addTransaction(transaction);

        assertNotNull(response);
        assertEquals("Transaction successful", response.getMessage());
        assertEquals(transaction, response.getData());
        assertEquals(900.0, account.getAccountBalance());
    }

    @Test
    void testAddTransaction_InsufficientFunds() {
        account.setAccountBalance(50.0);
        when(accountRepository.findByAccountNumber("123456789")).thenReturn(account);

        transaction.setAmount(100.0);

        Exception exception = assertThrows(InsufficientFundsException.class, () -> {
            transactionService.addTransaction(transaction);
        });

        assertEquals("Insufficient funds for withdrawal. Account balance: 50.0, requested withdrawal: 100.0", exception.getMessage());
    }

    @Test
    void testAddDummyTransaction() {
        when(accountRepository.findByAccountNumber("123456789")).thenReturn(account);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        CustomResponse<Transaction> response = transactionService.addDummyTransaction(transaction);

        assertNotNull(response);
        assertEquals("Transaction successful", response.getMessage());
        assertEquals(transaction, response.getData());
    }

    @Test
    void testAddTransaction_BudgetExceedanceCheck() {
        when(accountRepository.findByAccountNumber("123456789")).thenReturn(account);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        CustomResponse<Transaction> response = transactionService.addTransaction(transaction);

        if (transaction.getTransactionType() == TransactionType.WITHDRAW) {
            verify(budgetServiceClient, times(1)).checkExceedance(transaction);
        }
        
        assertNotNull(response);
        assertEquals("Transaction successful", response.getMessage());
        assertEquals(transaction, response.getData());
    }

    @Test
    void testProcessTransaction_AccountNotFound() {
        when(accountRepository.findByAccountNumber("123456789")).thenReturn(null);

        CustomResponse<Transaction> response = transactionService.addTransaction(transaction);

        assertNotNull(response);
        assertEquals("No account found for the transaction: 123456789", response.getMessage());
        assertNull(response.getData());
    }
}

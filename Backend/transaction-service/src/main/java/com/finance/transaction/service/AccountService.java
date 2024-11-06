package com.finance.transaction.service;

import com.finance.transaction.model.Account;
import com.finance.transaction.dto.CustomResponse;
import com.finance.transaction.model.CategoryType;
import com.finance.transaction.model.Transaction;
import com.finance.transaction.model.TransactionType;
import com.finance.transaction.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    public CustomResponse<List<Account>> getAccountsByUserId(Long userId) {
        List<Account> accounts = accountRepository.findAllByUserId(userId);
        if (accounts.isEmpty()) {
            return new CustomResponse<>("No accounts found for userId: " + userId, null);
        }
        return new CustomResponse<>("Accounts retrieved successfully", accounts);
    }

    public CustomResponse<Account> addAccount(Account account) {
        Account createdAccount = accountRepository.save(account);
        createDummyTransactions(createdAccount);
        return new CustomResponse<>("Account created successfully", createdAccount);
    }

    private void createDummyTransactions(Account account) {
        List<Transaction> transactions = new ArrayList<>();

        // Create some dummy transactions
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // Start from today's date
        transactions.add(new Transaction(null, 150.0, account.getUserId(), "Salary deposit", account.getAccountNumber(), calendar.getTime(), TransactionType.DEPOSIT, CategoryType.INCOME, account));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        transactions.add(new Transaction(null, 30.0, account.getUserId(), "Grocery shopping", account.getAccountNumber(), calendar.getTime(), TransactionType.WITHDRAW, CategoryType.FOOD, account));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        transactions.add(new Transaction(null, 15.0, account.getUserId(), "Bus fare", account.getAccountNumber(), calendar.getTime(), TransactionType.WITHDRAW, CategoryType.TRANSPORTATION, account));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        transactions.add(new Transaction(null, 500.0, account.getUserId(), "Rent payment", account.getAccountNumber(), calendar.getTime(), TransactionType.WITHDRAW, CategoryType.HOUSING, account));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        transactions.add(new Transaction(null, 200.0, account.getUserId(), "Bonus received", account.getAccountNumber(), calendar.getTime(), TransactionType.DEPOSIT, CategoryType.INCOME, account));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        transactions.add(new Transaction(null, 40.0, account.getUserId(), "Dinner out", account.getAccountNumber(), calendar.getTime(), TransactionType.WITHDRAW, CategoryType.ENTERTAINMENT, account));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        transactions.add(new Transaction(null, 20.0, account.getUserId(), "Gas for car", account.getAccountNumber(), calendar.getTime(), TransactionType.WITHDRAW, CategoryType.TRANSPORTATION, account));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        transactions.add(new Transaction(null, 10.0, account.getUserId(), "Coffee shop", account.getAccountNumber(), calendar.getTime(), TransactionType.WITHDRAW, CategoryType.ENTERTAINMENT, account));

        transactionService.addTransactions(transactions);
    }

    public CustomResponse<Account> updateAccount(Long accountId, Account account) {
        Optional<Account> existingAccountOpt = accountRepository.findById(accountId);
        if (existingAccountOpt.isEmpty()) {
            return new CustomResponse<>("Account not found for accountId: " + accountId, null);
        }

        Account existingAccount = existingAccountOpt.get();
        existingAccount.setAccountNumber(account.getAccountNumber());
        existingAccount.setAccountBalance(account.getAccountBalance());
        // Update other fields as needed

        Account updatedAccount = accountRepository.save(existingAccount);
        return new CustomResponse<>("Account updated successfully", updatedAccount);
    }

    public CustomResponse<Account> deleteAccount(Long accountId) {
        // Check if the account exists
        if (!accountRepository.existsById(accountId)) {
            return new CustomResponse<>("Account not found for accountId: " + accountId, null);
        }

        // Fetch the account to be deleted
        Account accountToDelete = accountRepository.findById(accountId).orElse(null);

        // Delete the account
        accountRepository.deleteById(accountId);

        // Return the deleted account details in the response
        return new CustomResponse<>("Account deleted successfully", accountToDelete);
    }

}

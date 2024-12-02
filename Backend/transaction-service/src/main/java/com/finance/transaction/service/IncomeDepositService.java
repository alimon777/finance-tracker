package com.finance.transaction.service;

import com.finance.transaction.dto.IncomeDepositDTO;
import com.finance.transaction.model.Transaction;
import com.finance.transaction.model.TransactionType;
import com.finance.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IncomeDepositService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Helper method to calculate total deposits and withdrawals
    private IncomeDepositDTO calculateDepositsAndWithdrawals(List<Transaction> transactions, String period) {
        double totalDeposits = transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.DEPOSIT)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalWithdrawals = transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.WITHDRAW)
                .mapToDouble(Transaction::getAmount)
                .sum();

        return new IncomeDepositDTO(period, totalDeposits, totalWithdrawals);
    }

    // Helper method to group transactions by date
    private Map<LocalDate, List<Transaction>> groupTransactionsByDate(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getTransactionDate));
    }

    // Helper method to get transactions for a specific time range
    private List<Transaction> getTransactionsForPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByUserIdAndTransactionDateBetween(
                userId,
                startDate,
                endDate
        );
    }

    public List<IncomeDepositDTO> getWeeklyDepositsAndWithdrawals(Long userId) {
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentWeek = today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);

        Map<LocalDate, List<Transaction>> groupedByDate = transactions.stream()
                .filter(transaction -> {
                    LocalDate transactionDate = transaction.getTransactionDate();
                    int transactionYear = transactionDate.getYear();
                    int transactionWeek = transactionDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                    return transactionYear == currentYear && transactionWeek == currentWeek;
                })
                .collect(Collectors.groupingBy(Transaction::getTransactionDate));

        return groupedByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Sort by date
                .map(entry -> calculateDepositsAndWithdrawals(entry.getValue(), entry.getKey().toString()))
                .collect(Collectors.toList());
    }

    public List<IncomeDepositDTO> getMonthlyDepositsAndWithdrawals(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.with(TemporalAdjusters.firstDayOfMonth());

        List<Transaction> transactions = getTransactionsForPeriod(userId, monthStart, today);

        Map<LocalDate, List<Transaction>> groupedByDate = groupTransactionsByDate(transactions);

        return groupedByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Sort by date
                .map(entry -> calculateDepositsAndWithdrawals(entry.getValue(), entry.getKey().toString()))
                .collect(Collectors.toList());
    }

    public List<IncomeDepositDTO> getYearlyDepositsAndWithdrawals(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate yearStart = today.with(TemporalAdjusters.firstDayOfYear());

        List<Transaction> transactions = getTransactionsForPeriod(userId, yearStart, today);

        Map<String, List<Transaction>> groupedByMonth = transactions.stream()
                .collect(Collectors.groupingBy(t -> t.getTransactionDate().getMonth().toString()));

        return groupedByMonth.entrySet().stream()
                .sorted(Map.Entry.comparingByKey((month1, month2) ->
                        Month.valueOf(month1).compareTo(Month.valueOf(month2)))) // Sort by month order
                .map(entry -> calculateDepositsAndWithdrawals(entry.getValue(), entry.getKey()))
                .collect(Collectors.toList());
    }
}

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
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IncomeDepositService {

    @Autowired
    private TransactionRepository transactionRepository;

    public LocalDate convertToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return new java.util.Date(date.getTime()).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public List<IncomeDepositDTO> getWeeklyDepositsAndWithdrawals(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(6); // Last 7 days, including today

        List<Transaction> transactions = transactionRepository.findByUserIdAndTransactionDateBetween(
                userId, Date.from(weekStart.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant())
        );

        Map<LocalDate, List<Transaction>> groupedByDate = transactions.stream()
                .collect(Collectors.groupingBy(t -> convertToLocalDate(t.getTransactionDate())));

        return groupedByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Sort by date
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<Transaction> dailyTransactions = entry.getValue();

                    double totalDeposits = dailyTransactions.stream()
                            .filter(t -> t.getTransactionType() == TransactionType.DEPOSIT)
                            .mapToDouble(Transaction::getAmount)
                            .sum();

                    double totalWithdrawals = dailyTransactions.stream()
                            .filter(t -> t.getTransactionType() == TransactionType.WITHDRAW)
                            .mapToDouble(Transaction::getAmount)
                            .sum();

                    return new IncomeDepositDTO(date.toString(), totalDeposits, totalWithdrawals);
                })
                .collect(Collectors.toList());
    }

    public List<IncomeDepositDTO> getMonthlyDepositsAndWithdrawals(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.with(TemporalAdjusters.firstDayOfMonth());

        List<Transaction> transactions = transactionRepository.findByUserIdAndTransactionDateBetween(
                userId, Date.from(monthStart.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant())
        );

        Map<LocalDate, List<Transaction>> groupedByDate = transactions.stream()
                .collect(Collectors.groupingBy(t -> convertToLocalDate(t.getTransactionDate())));

        return groupedByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Sort by date
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<Transaction> dailyTransactions = entry.getValue();

                    double totalDeposits = dailyTransactions.stream()
                            .filter(t -> t.getTransactionType() == TransactionType.DEPOSIT)
                            .mapToDouble(Transaction::getAmount)
                            .sum();

                    double totalWithdrawals = dailyTransactions.stream()
                            .filter(t -> t.getTransactionType() == TransactionType.WITHDRAW)
                            .mapToDouble(Transaction::getAmount)
                            .sum();

                    return new IncomeDepositDTO(date.toString(), totalDeposits, totalWithdrawals);
                })
                .collect(Collectors.toList());
    }

    public List<IncomeDepositDTO> getYearlyDepositsAndWithdrawals(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate yearStart = today.with(TemporalAdjusters.firstDayOfYear());

        List<Transaction> transactions = transactionRepository.findByUserIdAndTransactionDateBetween(
                userId, Date.from(yearStart.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant())
        );

        Map<String, List<Transaction>> groupedByMonth = transactions.stream()
                .collect(Collectors.groupingBy(t -> convertToLocalDate(t.getTransactionDate()).getMonth().toString()));

        return groupedByMonth.entrySet().stream()
                .sorted(Map.Entry.comparingByKey((month1, month2) ->
                        Month.valueOf(month1).compareTo(Month.valueOf(month2)))) // Sort by month order
                .map(entry -> {
                    String month = entry.getKey();
                    List<Transaction> monthlyTransactions = entry.getValue();

                    double totalDeposits = monthlyTransactions.stream()
                            .filter(t -> t.getTransactionType() == TransactionType.DEPOSIT)
                            .mapToDouble(Transaction::getAmount)
                            .sum();

                    double totalWithdrawals = monthlyTransactions.stream()
                            .filter(t -> t.getTransactionType() == TransactionType.WITHDRAW)
                            .mapToDouble(Transaction::getAmount)
                            .sum();

                    return new IncomeDepositDTO(month, totalDeposits, totalWithdrawals);
                })
                .collect(Collectors.toList());
    }
}

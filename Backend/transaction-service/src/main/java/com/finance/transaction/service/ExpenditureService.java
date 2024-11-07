package com.finance.transaction.service;

import com.finance.transaction.model.Transaction;
import com.finance.transaction.model.TransactionType;
import com.finance.transaction.repository.TransactionRepository;
import com.finance.transaction.dto.ExpenditureSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExpenditureService {

    @Autowired
    private TransactionRepository transactionRepository;

    public ExpenditureSummaryDTO getExpenditureSummary(Long userId) {
        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);

        ExpenditureSummaryDTO summary = new ExpenditureSummaryDTO();

        Map<String, Double> weeklyDepositMap = new HashMap<>();
        Map<String, Double> weeklyWithdrawMap = new HashMap<>();
        Map<String, Double> monthlyDepositMap = new HashMap<>();
        Map<String, Double> monthlyWithdrawMap = new HashMap<>();
        Map<String, Double> yearlyDepositMap = new HashMap<>();
        Map<String, Double> yearlyWithdrawMap = new HashMap<>();

        double weeklyWithdrawTotal = 0.0;
        double monthlyWithdrawTotal = 0.0;
        double yearlyWithdrawTotal = 0.0;

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        for (Transaction transaction : transactions) {
            calendar.setTime(transaction.getTransactionDate());
            int transactionYear = calendar.get(Calendar.YEAR);
            int transactionMonth = calendar.get(Calendar.MONTH);
            int transactionWeek = calendar.get(Calendar.WEEK_OF_YEAR);

            String category = transaction.getCategoryType().name();
            double amount = transaction.getAmount();

            if (transactionYear == currentYear) {
                if (transaction.getTransactionType() == TransactionType.WITHDRAW) {
                    yearlyWithdrawMap.put(category, yearlyWithdrawMap.getOrDefault(category, 0.0) + amount);
                    yearlyWithdrawTotal += amount;
                } else {
                    yearlyDepositMap.put(category, yearlyDepositMap.getOrDefault(category, 0.0) + amount);
                }

                if (transactionMonth == currentMonth) {
                    if (transaction.getTransactionType() == TransactionType.WITHDRAW) {
                        monthlyWithdrawMap.put(category, monthlyWithdrawMap.getOrDefault(category, 0.0) + amount);
                        monthlyWithdrawTotal += amount;
                    } else {
                        monthlyDepositMap.put(category, monthlyDepositMap.getOrDefault(category, 0.0) + amount);
                    }

                    if (transactionWeek == currentWeek) {
                        if (transaction.getTransactionType() == TransactionType.WITHDRAW) {
                            weeklyWithdrawMap.put(category, weeklyWithdrawMap.getOrDefault(category, 0.0) + amount);
                            weeklyWithdrawTotal += amount;
                        } else {
                            weeklyDepositMap.put(category, weeklyDepositMap.getOrDefault(category, 0.0) + amount);
                        }
                    }
                }
            }
        }

        summary.setWeekly(new ExpenditureSummaryDTO.ExpenditureDetail(weeklyDepositMap, weeklyWithdrawMap, weeklyWithdrawTotal));
        summary.setMonthly(new ExpenditureSummaryDTO.ExpenditureDetail(monthlyDepositMap, monthlyWithdrawMap, monthlyWithdrawTotal));
        summary.setYearly(new ExpenditureSummaryDTO.ExpenditureDetail(yearlyDepositMap, yearlyWithdrawMap, yearlyWithdrawTotal));

        return summary;
    }
}
